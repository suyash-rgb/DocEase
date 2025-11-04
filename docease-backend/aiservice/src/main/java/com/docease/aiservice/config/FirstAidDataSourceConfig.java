package com.docease.aiservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

// src/main/java/com/docease/aiservice/config/FirstAidDataSourceConfig.java
@Configuration
@EnableJpaRepositories(
        basePackages = "com.docease.aiservice.repository.firstaid",
        entityManagerFactoryRef = "firstAidEntityManager",
        transactionManagerRef = "firstAidTransactionManager"
)
public class FirstAidDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.firstaid")
    public DataSource firstAidDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean firstAidEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(firstAidDataSource());
        em.setPackagesToScan("com.docease.aiservice.entity.firstaid");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Explicitly set Hibernate properties
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.setProperty("hibernate.hbm2ddl.auto", "validate");  // or from yml if preferred
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(props);

        return em;
    }

    @Bean
    public PlatformTransactionManager firstAidTransactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(firstAidEntityManager().getObject());
        return tm;
    }
}
