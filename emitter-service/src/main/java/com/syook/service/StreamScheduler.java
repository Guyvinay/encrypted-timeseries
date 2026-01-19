package com.syook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.StringJoiner;

@Slf4j
@Component
public class StreamScheduler {

    private final SocketClient socketClient;
    private final MessageGenerator generator;
    private final HashEncryptionService cryptoService;

    @Value("${emitter.batch.min}")
    private int batchMin;

    @Value("${emitter.batch.max}")
    private int batchMax;

    private final Random random = new Random();

    public StreamScheduler(SocketClient socketClient, MessageGenerator generator, HashEncryptionService cryptoService) {
        this.socketClient = socketClient;
        this.generator = generator;
        this.cryptoService = cryptoService;
    }

    @Scheduled(fixedRateString = "${emitter.schedule.rate-ms}")
    public void emit() {

        int count = random.nextInt(batchMax - batchMin) + batchMin;

        StringJoiner joiner = new StringJoiner("|");

        for (int i = 0; i < count; i++) {

            String plain = generator.generatePlainMessage();
            String encrypted = cryptoService.encrypt(plain);

            joiner.add(encrypted);
        }

        String stream = joiner.toString() + "\n";

        socketClient.send(stream);
        log.info("Emit batch size: {}", count);

    }
}