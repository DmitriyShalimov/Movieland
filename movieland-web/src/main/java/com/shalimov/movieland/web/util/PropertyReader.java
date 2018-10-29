package com.shalimov.movieland.web.util;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

import static java.lang.Thread.currentThread;

public class PropertyReader {
    private final Logger logger = getLogger(getClass());
    private String propertyFileName;

    public PropertyReader(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public Properties readProperties() {
        logger.info("reading properties from file {}", propertyFileName);
        Properties prop = new Properties();
        try (final InputStream inputStream = currentThread()
                .getContextClassLoader()
                .getResourceAsStream(propertyFileName)) {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error("error {} occurred during reading properties file {}", e.getMessage(), propertyFileName);
            throw new RuntimeException(e);
        }
        return prop;
    }
}
