package com.shalimov.movieland.web.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableScheduling
@PropertySource("classpath:properties/application.yml")
@ComponentScan(basePackages = "com.shalimov.movieland",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "com\\.shalimov\\.movieland\\web\\.controller.*"))
public class RootConfig {

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    ExecutorService executorService() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        taskExecutor.initialize();
        return new ExecutorServiceAdapter(taskExecutor);
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PGSimpleDataSource dataSource(@Value("${jdbc.password}") String password, @Value("${jdbc.port}") String port,
                                  @Value("${jdbc.databaseName}") String databaseName, @Value("${jdbc.user}") String user) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPassword(password);
        dataSource.setPortNumber(Integer.parseInt(port));
        dataSource.setUser(user);
        dataSource.setDatabaseName(databaseName);
        return dataSource;
    }


}
