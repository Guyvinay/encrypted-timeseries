package com.syook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syook.dto.BucketStatsDto;
import com.syook.dto.MessageRecordDto;
import com.syook.entity.MessageBucketEntity;
import com.syook.repo.MessageBucketRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class StatsService {

    private final MessageBucketRepository repository;
    private final ObjectMapper mapper;

    public StatsService(MessageBucketRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public BucketStatsDto getLatestBucketStats() {

        Instant nowBucket = Instant.now().truncatedTo(ChronoUnit.MINUTES);

        Optional<MessageBucketEntity> opt = repository.findBucketForUpdate(nowBucket);

        if (opt.isEmpty()) {
            return new BucketStatsDto(nowBucket, 0, 0, 0.0);
        }

        MessageBucketEntity entity = opt.get();

        double successRate = entity.getTotalCount() == 0 ?
                0.0 :
                ((double) entity.getValidCount() / entity.getTotalCount());

        return new BucketStatsDto(
                nowBucket,
                entity.getTotalCount(),
                entity.getValidCount(),
                successRate
        );
    }

    public List<MessageRecordDto> getLatestRecords() {

        Instant nowBucket = Instant.now().truncatedTo(ChronoUnit.MINUTES);

        Optional<MessageBucketEntity> opt = repository.findBucketForUpdate(nowBucket);

        if (opt.isEmpty()) return List.of();

        MessageBucketEntity entity = opt.get();

        try {
            List<Map<String, Object>> raw = mapper.readValue(entity.getRecords(), List.class);

            List<MessageRecordDto> result = new ArrayList<>();

            for (Map<String, Object> record : raw) {

                Instant timestamp = record.containsKey("timestamp") ?
                        Instant.parse(record.get("timestamp").toString()) :
                        Instant.now();

                result.add(new MessageRecordDto(
                        record.get("name").toString(),
                        record.get("origin").toString(),
                        record.get("destination").toString(),
                        timestamp
                ));
            }

            return result;

        } catch (Exception e) {
            return List.of();
        }
    }
}