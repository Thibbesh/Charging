package com.evbox.charging.service;

import com.evbox.charging.model.ChargingSession;

import java.util.List;

/**
 * Interface for the chargingSessions
 */

public interface ChargingService {

    /**
     * This method to start chargingSessions
     * @return chargingSession
     */
    ChargingSession submitChargingSessions(String stationId);

    /**
     * This method used to getAllChargingSessions
     * @return list of chargingSessions
     */
    List<ChargingSession> getAllChargingSessions();

    /**
     * This method used to stop the existing sessions
     * @return chargingSession
     */
    ChargingSession stopChargingSessions(String id);


}
