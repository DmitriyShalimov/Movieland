package com.shalimov.movieland.service.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyCacheService {
    private final Environment environment;

    @Autowired
    public CurrencyCacheService(Environment environment) {
        this.environment = environment;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Map<String, Double> rates = new ConcurrentHashMap<>();

    public Map<String, Double> getRates() {
        return new HashMap<>(rates);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void getRatesFromAPI() {
        try (InputStream inputStream = new URL(environment.getProperty("currency.rate.url")).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(reader);
            for (JsonNode node : jsonNode) {
                String currency = node.findValue("cc").toString();
                Double rate = Double.valueOf(node.findValue("rate").toString());
                if ("\"USD\"".equals(currency) || "\"EUR\"".equals(currency)) {
                    rates.put(currency, rate);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting json", e);
        }
    }
}
