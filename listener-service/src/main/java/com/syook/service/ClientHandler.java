package com.syook.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final MessageProcessorService messageProcessorService;


    public ClientHandler(Socket socket, MessageProcessorService messageProcessorService) {
        this.socket = socket;
        this.messageProcessorService = messageProcessorService;
    }

    @Override
    public void run() {

        log.info("Client handler thread started");

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            String line = reader.readLine();
            log.info("Received raw stream length={}", line.length());
            messageProcessorService.process(line);
            log.info("Stream processing completed successfully");

        } catch (Exception e) {
            log.error("Client error: {}", e.getMessage());
        }
    }
}