package com.example.gatewayservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Log4j2
public class CustomCircuitBreakerConfig {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCircuitBreakerCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> customCircuitBreakerCustomizer() {

        log.info("ALL CB REGISTERED: {}", circuitBreakerRegistry.getAllCircuitBreakers());
        log.info("DEFAULT CB CONFIG: {}", circuitBreakerRegistry.getDefaultConfig());
        return factory -> {
            factory.configure(builder -> builder
                    .timeLimiterConfig(getTimeLimiterConfig())
                    .circuitBreakerConfig(getCircuitBreakerConfig()), "USER-SERVICE, TODO-SERVICE");
            log.info("USER-SERVICE CB CONFIG: {}", circuitBreakerRegistry.circuitBreaker("USER-SERVICE").getCircuitBreakerConfig());
            log.info("TODO-SERVICE CB CONFIG: {}", circuitBreakerRegistry.circuitBreaker("TODO-SERVICE").getCircuitBreakerConfig());
        };
    }

    public TimeLimiterConfig getTimeLimiterConfig() {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();
    }

    public CircuitBreakerConfig getCircuitBreakerConfig() {
        return CircuitBreakerConfig.from(circuitBreakerRegistry.getDefaultConfig())
                .failureRateThreshold(10)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .slidingWindowSize(10)
                .minimumNumberOfCalls(10)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(5))
                .permittedNumberOfCallsInHalfOpenState(5)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
    }

}
