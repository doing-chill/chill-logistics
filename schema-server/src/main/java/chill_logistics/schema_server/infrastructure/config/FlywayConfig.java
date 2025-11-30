package chill_logistics.schema_server.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class FlywayConfig {

    @Bean
    public Flyway mysql1Flyway(
            @Qualifier("mysql1DataSource") DataSource ds) {
        log.info("Running Flyway migration for mysql1...");
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .locations("classpath:db/migration/mysql1")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        log.info("Completed mysql1 migration.");
        return flyway;
    }

    @Bean
    public Flyway mysql2Flyway(
            @Qualifier("mysql2DataSource") DataSource ds) {
        log.info("Running Flyway migration for mysql2...");
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .locations("classpath:db/migration/mysql2")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        log.info("Completed mysql2 migration.");
        return flyway;
    }

    @Bean
    public Flyway mysql3Flyway(
            @Qualifier("mysql3DataSource") DataSource ds) {
        log.info("Running Flyway migration for mysql3...");
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .locations("classpath:db/migration/mysql3")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        log.info("Completed mysql3 migration.");
        return flyway;
    }
}
