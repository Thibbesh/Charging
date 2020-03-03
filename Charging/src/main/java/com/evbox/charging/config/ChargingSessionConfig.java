package com.evbox.charging.config;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.store.ChargingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this config class to store the ChargingSession <br>
 *
 */
@Configuration
public class ChargingSessionConfig {

    @Bean
    public ChargingStore chargingSessionStore() {
        return new ChargingStore(new ConcurrentHashMap<UUID, ChargingSession>());
    }
}
