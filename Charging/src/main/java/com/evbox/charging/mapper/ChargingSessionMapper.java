package com.evbox.charging.mapper;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.StatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This mapper class to map the model object.
 */

public final class ChargingSessionMapper {

    private ChargingSessionMapper() {

    }

    public static ChargingSession map(String stationId, UUID uuid) {
        return ChargingSession.builder()
                                          .id(uuid)
                                          .stationId(stationId)
                                          .startedAt(LocalDateTime.now())
                                          .status(StatusEnum.IN_PROGRESS)
                                          .build();
    }
}
