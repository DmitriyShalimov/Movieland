package com.shalimov.movieland.web.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@PropertySource("classpath:properties/application.properties")
@ComponentScan(basePackages = "com.shalimov.movieland",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "com\\.shalimov\\.movieland\\web\\.controller.*"))
public class RootConfig {


    private final Environment environment;

    @Autowired
    public RootConfig(Environment environment) {
        this.environment = environment;
    }

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
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        dataSource.setPortNumber(Integer.parseInt(environment.getProperty("jdbc.port")));
        dataSource.setUser(environment.getProperty("jdbc.user"));
        dataSource.setDatabaseName(environment.getProperty("jdbc.databaseName"));
        return dataSource;
    }


}
