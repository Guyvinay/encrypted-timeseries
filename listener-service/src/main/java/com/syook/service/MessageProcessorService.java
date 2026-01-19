package com.syook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syook.dto.MessagePayload;
import com.syook.entity.MessageBucketEntity;
import com.syook.repo.MessageBucketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class MessageProcessorService {

    private final AesCryptoService cryptoService;
    private final MessageBucketRepository repository;

    private final ObjectMapper mapper;

    public MessageProcessorService(
            AesCryptoService cryptoService,
            MessageBucketRepository repository,
            ObjectMapper mapper
    ) {
        this.cryptoService = cryptoService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public void process(String stream) {
        log.info("Processing incoming stream");

        String[] messages = stream.split("\\|");
        log.info("Total encrypted messages received={}", messages.length);

        Instant bucket =
                Instant.now().truncatedTo(ChronoUnit.MINUTES);

        List<Map<String, Object>> validRecords = new ArrayList<>();

        int total = 0;
        int valid = 0;

        for (String encrypted : messages) {

            total++;

            try {
                String json = cryptoService.decrypt(encrypted);

                MessagePayload payload =
                        mapper.readValue(json, MessagePayload.class);

                String recalculated = cryptoService.sha256(
                        payload.getName()
                                + payload.getOrigin()
                                + payload.getDestination()
                );

                if (!recalculated.equals(payload.getSecret_key())) {
                    log.warn("Secret key did not match, skipping");
                    continue;
                }

                Map<String, Object> record = new HashMap<>();
                record.put("name", payload.getName());
                record.put("origin", payload.getOrigin());
                record.put("destination", payload.getDestination());
                record.put("timestamp", Instant.now());

                validRecords.add(record);
                valid++;

            } catch (Exception e) {
                log.error("Error {}", e.getMessage(), e);
            }
        }
        log.info("Stream summary: total={} valid={} invalid={}",
                total,
                valid,
                total - valid);
        saveBucket(bucket, validRecords, total, valid);
    }

    private void saveBucket(
            Instant bucket,
            List<Map<String, Object>> records,
            int total,
            int valid
    ) {
        log.info("Persisting bucket data bucketTime={}", bucket);

        MessageBucketEntity entity =
                repository.findBucketForUpdate(bucket)
                        .orElseGet(MessageBucketEntity::new);

        entity.setBucketTime(bucket);
        entity.setTotalCount(entity.getTotalCount() + total);
        entity.setValidCount(entity.getValidCount() + valid);

        try {
            String existing =
                    entity.getRecords() == null ? "[]" : entity.getRecords();

            List<Map<String, Object>> all =
                    mapper.readValue(existing, List.class);

            all.addAll(records);

            entity.setRecords(mapper.writeValueAsString(all));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        repository.save(entity);
        log.info("Bucket persisted successfully bucketTime={}", bucket);

    }
}