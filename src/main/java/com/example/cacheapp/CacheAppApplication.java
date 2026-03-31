package com.example.cacheapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class CacheAppApplication {
    static {
        // Fix: Railway PostgreSQL không nhận "Asia/Saigon"
        // Phải override timezone TRƯỚC khi JDBC driver kết nối
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    public static void main(String[] args) {
        SpringApplication.run(CacheAppApplication.class, args);
    }

}
