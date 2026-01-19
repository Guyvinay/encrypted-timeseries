package com.syook.listener;

import com.syook.service.ClientHandler;
import com.syook.service.MessageProcessorService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class TcpServer {

    @Value("${listener.socket.port}")
    private int port;

    @Autowired
    @Qualifier("tcpAcceptExecutor")
    private Executor acceptExecutor;

    @Autowired
    @Qualifier("tcpWorkerExecutor")
    private Executor workerExecutor;

    @Autowired
    private MessageProcessorService messageProcessorService;


    private volatile boolean isRunning = true;

    private ServerSocket serverSocket;

    @PostConstruct
    public void start() {
        acceptExecutor.execute(()-> {
            try {
                serverSocket = new ServerSocket(port);

                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    workerExecutor.execute(new ClientHandler(socket, messageProcessorService));
                }

            } catch (Exception e) {
                log.error("TCP crashed", e);
            }
        });
    }

    @PreDestroy
    public void shutdown() throws IOException {
        log.info("TCP Server shutting down");
        isRunning = false;
        serverSocket.close();
    }
}