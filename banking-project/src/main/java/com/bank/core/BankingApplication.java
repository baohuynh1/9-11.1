package com.bank.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class BankingApplication {

    private static final Logger log = LoggerFactory.getLogger(BankingApplication.class);

    private final JdbcTemplate jdbcTemplate;

    public BankingApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    /** In ra log tên database & phiên bản PostgreSQL để chứng minh kết nối thành công. */
    @Bean
    CommandLineRunner verifyDatabaseConnection() {
        return args -> {
            String db = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
            String version = jdbcTemplate.queryForObject("SHOW server_version", String.class);
            log.info(">>> Ket noi thanh cong toi database: {} (PostgreSQL {})", db, version);
        };
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {
        String db = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        return Map.of("status", "UP", "database", db);
    }
}
