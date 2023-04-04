package pl.artur.shopsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import supplier.TimeSupplier;

import java.time.LocalDateTime;

@Configuration
public class TimeConfig {
    @Bean
    TimeSupplier timeSupplier() {
        return LocalDateTime::now;
    }
}
