package com.runaumov.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.runaumov.spring")
public class HibernateConfig {

    @Value("${db.driver.class.name}")
    private String driverClassName;
    @Value("${db.jdbc.url}")
    private String jdbcUrl;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.maximum.pool.size:10}")
    private int maximumPoolSize;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.show.sql}")
    private String showSql;
    @Value("${hibernate.format.sql}")
    private String formatSql;


    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.runaumov.spring.entity");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, hibernateDialect);
        hibernateProperties.put(Environment.SHOW_SQL, showSql);
        hibernateProperties.put(Environment.FORMAT_SQL, formatSql);

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

}
