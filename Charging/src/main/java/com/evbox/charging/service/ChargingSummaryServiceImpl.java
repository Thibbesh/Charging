package com.evbox.charging.service;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionSummary;
import com.evbox.charging.model.StatusEnum;
import com.evbox.charging.model.store.ChargingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ChargingSummaryServiceImpl is the SessionSummary of all
 * <p>StartedAt</p> <p>StoppedAt</p> <p>totalCount</p> of chargingSession
 * within a minutes.
 */
@Service
@RequiredArgsConstructor
public class ChargingSummaryServiceImpl implements  ChargingSummaryService{


    private final ChargingStore sessionStore;

    private static final Duration MIN = Duration.ofMinutes(1L);

    /**
     * This method will calculate last minute summary of </br>
     * chargingSessions.
     *
     * @return ChargingSessionSummary
     */
    @Override
    public ChargingSessionSummary getSummaryForLastMinute() {
        LocalDateTime oneMinBefore = LocalDateTime.now().minus(MIN);

        long startedCount = countStartedAt(oneMinBefore);
        long stoppedCount = countStoppedAt(oneMinBefore);

        return ChargingSessionSummary.builder()
                              .startedCount(startedCount)
                              .stoppedCount(stoppedCount)
                              .totalCount(startedCount+stoppedCount)
                              .build();
    }

    private long countStoppedAt(LocalDateTime oneMinBefore) {
        return sessionStore.getSessions().values().stream()
                                                .filter(s -> (s.getStatus() == StatusEnum.FINISHED && s.getStoppedAt().isAfter(oneMinBefore))).count();
    }

    private long countStartedAt(LocalDateTime oneMinBefore) {
        return sessionStore.getSessions().values().stream()
                                                  .map(ChargingSession::getStartedAt)
                                                  .filter(Objects::nonNull)
                                                  .filter(startTime -> startTime.isAfter(oneMinBefore)).count();
    }
}
