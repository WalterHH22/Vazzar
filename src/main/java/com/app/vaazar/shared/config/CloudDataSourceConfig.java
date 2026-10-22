package com.app.vaazar.shared.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class CloudDataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/vaazar}") String url,
            @Value("${spring.datasource.username:vaazar}") String username,
            @Value("${spring.datasource.password:vaazar}") String password,
            @Value("${DATABASE_URL:}") String databaseUrl) {

        HikariConfig config = new HikariConfig();

        if (databaseUrl != null && !databaseUrl.isBlank() && 
            (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            try {
                URI uri = new URI(databaseUrl);
                String host = uri.getHost();
                int port = uri.getPort() > 0 ? uri.getPort() : 5432;
                String path = uri.getPath();
                String dbName = (path != null && path.startsWith("/")) ? path.substring(1) : path;

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                    jdbcUrl += "?" + uri.getQuery();
                } else if (host != null && !host.equals("localhost") && !host.equals("127.0.0.1")) {
                    jdbcUrl += "?sslmode=require";
                }
                config.setJdbcUrl(jdbcUrl);

                if (uri.getUserInfo() != null) {
                    String[] userInfoParts = uri.getUserInfo().split(":", 2);
                    config.setUsername(userInfoParts[0]);
                    if (userInfoParts.length > 1) {
                        config.setPassword(userInfoParts[1]);
                    }
                }
            } catch (Exception e) {
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
            }
        } else {
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
        }

        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }

    @Bean
    public org.springframework.beans.factory.config.BeanPostProcessor flywayRepairPostProcessor() {
        return new org.springframework.beans.factory.config.BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                if (bean instanceof org.flywaydb.core.Flyway flyway) {
                    try {
                        flyway.repair();
                    } catch (Exception ignored) {
                    }
                }
                return bean;
            }
        };
    }
}
