package com.shalimov.movieland.web.config;

import com.shalimov.movieland.web.util.PropertyReader;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.shalimov.movieland",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "com\\.shalimov\\.movieland\\web\\.controller.*"))
public class RootConfig {
    @Bean
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    PGSimpleDataSource dataSource() {
        Properties properties =
                new PropertyReader("properties/db.properties")
                        .readProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPassword(properties.getProperty("jdbc.password"));
        dataSource.setPortNumber(5432);
        dataSource.setUser(properties.getProperty("jdbc.user"));
        dataSource.setDatabaseName(properties.getProperty("jdbc.databaseName"));
        return dataSource;
    }


}
