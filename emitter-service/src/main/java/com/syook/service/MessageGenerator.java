package com.syook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syook.dto.MessagePayload;
import com.syook.utility.DataLoader;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageGenerator {

    private final DataLoader dataLoader;
    private final HashEncryptionService hashService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Random random = new Random();

    public MessageGenerator(DataLoader loader, HashEncryptionService hashService) {
        this.dataLoader = loader;
        this.hashService = hashService;
    }

    public String generatePlainMessage() {

        String name = pick(dataLoader.getNames());
        String origin = pick(dataLoader.getOrigins());
        String destination = pick(dataLoader.getDestinations());

        String raw = name + origin + destination;

        String checksum = hashService.sha256(raw);

        MessagePayload payload =
                new MessagePayload(name, origin, destination, checksum);

        try {
            return mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String pick(java.util.List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}