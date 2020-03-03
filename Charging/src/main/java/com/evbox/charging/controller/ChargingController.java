package com.evbox.charging.controller;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ChargingController handle below responsibility.
 * <p>getAllChargingSession</p>
 * p>StartChargingSession</p>
 * p>StopChargingSession</p>
 */

@RestController
public class ChargingController {

    @Autowired
    private ChargingService chargingService;



    /**
     * this method will get all chargingSessions
     * @return list of chargingSessions
     */
    @GetMapping("chargingSessions")
    public ResponseEntity<List<ChargingSession>> getChargingSessions() {
        List<ChargingSession> chargingSessionList = chargingService.getAllChargingSessions();
        return ResponseEntity.status(HttpStatus.OK).body(chargingSessionList);
    }

    /**
     * this method will start the chargingSessions.
     * @return chargingSession
     */
    @PostMapping("chargingSessions")
    @ResponseBody
    public ResponseEntity<ChargingSession> postChargingSessions(@RequestBody String stationId) {
        ChargingSession chargingSession = chargingService.submitChargingSessions(stationId);
        return ResponseEntity.status(HttpStatus.OK).body(chargingSession);
    }

    /**
     * this method will stop the existing chargingSessions.
     * @return chargingSession
     */
    @PutMapping("chargingSessions/{id}")
    public ResponseEntity<ChargingSession> putChargingSessions(@PathVariable String id) {
        ChargingSession chargingSession = chargingService.stopChargingSessions(id);
        return ResponseEntity.status(HttpStatus.OK).body(chargingSession);
    }
}
