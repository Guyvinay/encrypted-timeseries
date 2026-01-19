package com.syook.utility;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "emitter")
public class EmitterProperties {

    private Socket socket = new Socket();
    private Crypto crypto = new Crypto();
    private Batch batch = new Batch();
    private Schedule schedule = new Schedule();

    @Data
    public static class Socket {
        private String host;
        private int port;
    }

    @Data
    public static class Crypto {
        private String secret;
        private String salt;
    }

    @Data
    public static class Batch {
        private int min;
        private int max;
    }

    @Data
    public static class Schedule {
        private long rateMs;
    }
}
