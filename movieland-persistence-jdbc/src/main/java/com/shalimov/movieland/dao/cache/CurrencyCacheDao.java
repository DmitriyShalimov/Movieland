package com.shalimov.movieland.dao.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

@Service
@PropertySource("classpath:jdbc.application.properties")
public class CurrencyCacheDao {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private HashMap<String, Double> rates = new HashMap<>();

    public HashMap<String, Double> getRates() {
        return new HashMap<>(rates);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}")
    private void getRatesFromAPI() {
        String resultJson;
        try (InputStream inputStream = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json").openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            resultJson = stringBuilder.toString();
            JsonNode jsonNode = OBJECT_MAPPER.readTree(resultJson);
            for (JsonNode node : jsonNode) {
                if ("\"USD\"".equals(node.findValue("cc").toString())) {
                    rates.put("USD", Double.valueOf(node.findValue("rate").toString()));
                }
                if ("\"EUR\"".equals(node.findValue("cc").toString())) {
                    rates.put("EUR", Double.valueOf(node.findValue("rate").toString()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting json", e);
        }
    }
}
