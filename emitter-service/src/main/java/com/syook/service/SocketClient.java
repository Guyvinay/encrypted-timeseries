package com.syook.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SocketClient {

    @Value("${emitter.socket.port}")
    private int socketPort;

    @Value("${emitter.socket.host}")
    private String socketHost;

    public void send(String payload) {

        try (Socket socket = new Socket(socketHost, socketPort)) {

            OutputStream out = socket.getOutputStream();
            log.info("sending payload over socket: {}", payload);
            out.write(payload.getBytes(StandardCharsets.UTF_8));
            out.flush();

        } catch (Exception e) {
            log.error("Listener Exceptions {}:{}, {}", socketHost, socketHost, e.getMessage(), e);
        }
    }
}
