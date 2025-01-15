package com.forero.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.io.File;

@Configuration
@Profile("dev")
public class LocalDevConfig {

    @Bean
    public File tempDirectory() {
        File tempDir = new File("temp");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempDir;
    }
}