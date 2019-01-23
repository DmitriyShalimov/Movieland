package com.shalimov.movieland.web.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@PropertySource("classpath:properties/application.yml")
@ComponentScan(basePackages = "com.shalimov.movieland",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "com\\.shalimov\\.movieland\\web\\.controller.*"))
public class RootConfig {

    @Value("${jdbc.password}")
    String password;
    @Value("${jdbc.port}")
    String port;
    @Value("${jdbc.databaseName}")
    String databaseName;
    @Value("${jdbc.user}")
    String user;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.shalimov.movieland.dao.jpa"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    PGSimpleDataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPassword(password);
        dataSource.setPortNumber(Integer.parseInt(port));
        dataSource.setUser(user);
        dataSource.setDatabaseName(databaseName);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        return hibernateProperties;
    }


}
