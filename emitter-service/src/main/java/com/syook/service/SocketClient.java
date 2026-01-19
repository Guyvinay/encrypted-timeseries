package com.syook.service;

import com.syook.utility.EmitterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SocketClient {

    private final EmitterProperties props;

    public SocketClient(EmitterProperties props) {
        this.props = props;
    }

    public void send(String payload) {

        try (Socket socket =
                     new Socket(
                             props.getSocket().getHost(),
                             props.getSocket().getPort()
                     )) {

            OutputStream out = socket.getOutputStream();
            log.info("sending payload over socket: {}", payload);
            out.write(payload.getBytes(StandardCharsets.UTF_8));
            out.flush();

        } catch (Exception e) {
            log.error("Listener unavailable at {}:{}, {}", props.getSocket().getHost(), props.getSocket().getPort(), e.getMessage(), e);
        }
    }
}
