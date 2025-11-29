package chill_logistics.schema_server.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "mysql1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql1")
    public DataSource mysql1DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "mysql2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql2")
    public DataSource mysql2DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "mysql3DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql3")
    public DataSource mysql3DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
