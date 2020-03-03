package com.evbox.charging.service;

import com.evbox.charging.model.ChargingSessionSummary;

/**
 * Interface for chargingSummary
 */
public interface ChargingSummaryService {

    /**
     * This method get the summary for charging sessions within minute
     * @return chargingSessionSummary
     */
    ChargingSessionSummary getSummaryForLastMinute();
}
