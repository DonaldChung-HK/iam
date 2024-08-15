package it.infn.mw.iam.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    @Qualifier("dataSource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource primaryDataSource(@Qualifier("dataSource") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties("session.datasource")
    @Qualifier("sessionDataSourceProperties")
    @ConditionalOnProperty(name = "spring.session.store-type", havingValue = "jdbc")
    public DataSourceProperties sessionDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Qualifier("sessionDataSource")
    @SpringSessionDataSource
    @ConditionalOnProperty(name = "spring.session.store-type", havingValue = "jdbc")
    public HikariDataSource sessionDataSource(@Qualifier("sessionDataSourceProperties") DataSourceProperties sessionDataSourceProperties) {
        return sessionDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
