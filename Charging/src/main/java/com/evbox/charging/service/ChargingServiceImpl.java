package com.evbox.charging.service;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.StatusEnum;
import com.evbox.charging.model.store.ChargingStore;
import com.evbox.charging.exception.ChargingDataNotFoundException;
import com.evbox.charging.mapper.ChargingSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * this service class will handel the below responsibility.
 * <p>Start the session</p>
 * <p>Stop the sessions</p>
 * <p>get All the sessions</p>
 */
@Service
@RequiredArgsConstructor
public class ChargingServiceImpl implements ChargingService {

    private final ChargingStore sessionStore;

    /**
     * this method is to start the chargingSession with stationId.
     * @return chargingSession object
     */
    @Override
    public ChargingSession submitChargingSessions(String stationId) {
        final UUID uuid = UUID.randomUUID();
        ChargingSession chargingSession = ChargingSessionMapper.map(stationId, uuid);
        sessionStore.getSessions().put(uuid, chargingSession);
        return chargingSession;
    }

    /**
     * this method is to get all the active sessions.
     * @return list of chargingSessions
     */
    @Override
    public List<ChargingSession> getAllChargingSessions() {
        return sessionStore.getSessions().entrySet().stream()
                                                .map(Map.Entry::getValue)
                                                .collect(Collectors.toList());
    }

    /**
     * this method is stop the specific id of chargingSession
     * @param id of sessions
     * @return chargingSession
     */
    @Override
    public ChargingSession stopChargingSessions(String id) {
        final UUID uuid = UUID.fromString(id);
        final LocalDateTime stoppedAt = LocalDateTime.now();

        final ChargingSession chargingSession = sessionStore.getSessions().values().stream()
                                                            .filter(s -> (uuid.equals(s.getId()) && s.getStatus() == StatusEnum.IN_PROGRESS)).findAny()
                                                            .orElseThrow(() -> new ChargingDataNotFoundException("There is no in progress session with id: " + id));

        chargingSession.setStatus(StatusEnum.FINISHED);
        chargingSession.setStoppedAt(stoppedAt);

        return chargingSession;
    }

}
