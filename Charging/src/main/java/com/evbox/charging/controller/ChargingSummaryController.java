package com.evbox.charging.controller;

import com.evbox.charging.model.ChargingSessionSummary;
import com.evbox.charging.service.ChargingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ChargingSummaryController is to get the summary of the chargingSessions
 */
@RestController
public class ChargingSummaryController {

    @Autowired
    private ChargingSummaryService chargingSummaryService;

    /**
     * This method used to get charging Summary within a minutes.
     * @return chargingSessionSummary
     */
    @GetMapping("chargingSessions/summary")
    public ResponseEntity<ChargingSessionSummary> getChargingSessionsSummary() {
        ChargingSessionSummary chargingSessionSummary = chargingSummaryService.getSummaryForLastMinute();
        return ResponseEntity.status(HttpStatus.OK).body(chargingSessionSummary);
    }
}
