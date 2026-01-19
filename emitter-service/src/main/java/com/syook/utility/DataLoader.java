package com.syook.utility;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DataLoader {

    @Autowired
    private ObjectMapper OBJECT_MAPPER;

    @Getter
    private List<String> names;

    @Getter
    private List<String> origins;

    @Getter
    private List<String> destinations;

    @PostConstruct
    public void load() throws Exception {

        InputStream is = new ClassPathResource("data.json").getInputStream();

        Map<String, List<String>> data =
                OBJECT_MAPPER.readValue(is, Map.class);

        this.names = data.get("names");
        this.origins = data.get("origins");
        this.destinations = data.get("destinations");
        log.info("Data loaded names: {}, origins: {}, destination: {}", names.size(), origins.size(), destinations.size());
    }
}
