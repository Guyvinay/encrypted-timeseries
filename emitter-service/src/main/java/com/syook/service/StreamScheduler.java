package com.syook.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.StringJoiner;

@Component
@Slf4j
public class StreamScheduler {

    private final SocketClient socketClient;

    @Value("${emitter.batch.min}")
    private int batchMin;

    @Value("${emitter.batch.max}")
    private int batchMax;


    private final Random random = new Random();

    public StreamScheduler(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    @Scheduled(fixedRateString = "${emitter.schedule.rate-ms}")
    public void emit() {

        int count =
                random.nextInt(batchMax - batchMin) + batchMin;

        StringJoiner joiner = new StringJoiner("|");

        for (int i = 0; i < count; i++) {

            String encrypted = "Socket test payload";

            joiner.add(encrypted);
        }

        String stream = joiner.toString() + "\n";

        log.info("Emit batch size: {}", count);
        socketClient.send(stream);

    }
}