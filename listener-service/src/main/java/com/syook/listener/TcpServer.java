package com.syook.listener;

import com.syook.service.ClientHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TcpServer {

    @Value("${listener.socket.port}")
    private int port;

//    private final MessageProcessorService processorService;

    private final ExecutorService pool =
            Executors.newFixedThreadPool(10);

//    public TcpServer(MessageProcessorService processorService) {
//        this.processorService = processorService;
//    }

    @PostConstruct
    public void start() {

        new Thread(() -> {

            try (ServerSocket serverSocket =
                         new ServerSocket(port)) {

                System.out.println("Listener started on port " + port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    pool.submit(new ClientHandler(socket));
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}