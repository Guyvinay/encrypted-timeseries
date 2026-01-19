package com.syook.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            String line = reader.readLine();
            log.info("Received payload from socket: {}" , line);

        } catch (Exception e) {
            log.error("Client error: {}", e.getMessage());
        }
    }
}