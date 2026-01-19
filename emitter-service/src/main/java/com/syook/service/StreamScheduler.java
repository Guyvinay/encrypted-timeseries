package com.syook.service;

import com.syook.utility.EmitterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.StringJoiner;

@Component
@Slf4j
public class StreamScheduler {

    private final SocketClient socketClient;
    private final EmitterProperties props;

    private final Random random = new Random();

    public StreamScheduler(
            SocketClient socketClient,
            EmitterProperties props
    ) {
        this.socketClient = socketClient;
        this.props = props;
    }

    @Scheduled(fixedRateString = "${emitter.schedule.rate-ms}")
    public void emit() {

        int count =
                random.nextInt(
                        props.getBatch().getMax() -
                                props.getBatch().getMin()
                ) + props.getBatch().getMin();

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