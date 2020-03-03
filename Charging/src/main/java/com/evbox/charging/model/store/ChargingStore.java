package com.evbox.charging.model.store;

import com.evbox.charging.model.ChargingSession;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * ChargingStore is model class to store session.
 */
@Data
public class ChargingStore {

    private final Map<UUID, ChargingSession> sessions;
}
