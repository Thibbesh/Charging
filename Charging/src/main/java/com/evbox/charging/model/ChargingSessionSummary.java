package com.evbox.charging.model;

import lombok.*;

/**
 * ChargingSessionSummary is the model class to count startedAt, StoppedAt
 * and totalCount of chargingSession within minutes.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChargingSessionSummary {

    private long totalCount;
    private long startedCount;
    private long stoppedCount;
}
