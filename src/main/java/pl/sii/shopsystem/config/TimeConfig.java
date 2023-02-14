package pl.sii.shopsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sii.shopsystem.common.TimeSupplier;

import java.time.LocalDateTime;

@Configuration
public class TimeConfig {

    @Bean
    TimeSupplier timeSupplier() {
        return LocalDateTime::now;
    }
}
