package com.shalimov.movieland.service.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Map<String, Double> rates = new ConcurrentHashMap<>();
    private @Value("${currency.rate.url}")
    String url;

    public double getRate(String rate) {
        return rates.get(rate);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void getRatesFromAPI() {
        try (InputStream inputStream = new URL(url).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(reader);
            for (JsonNode node : jsonNode) {
                String currency = node.findValue("cc").toString();
                Double rate = Double.valueOf(node.findValue("rate").toString());
                if ("\"USD\"".equals(currency) || "\"EUR\"".equals(currency)) {
                    rates.put(currency.substring(1, currency.length() - 1), rate);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting json", e);
        }
    }
}
