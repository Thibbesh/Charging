package com.evbox.charging.service;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionSummary;
import com.evbox.charging.model.StatusEnum;
import com.evbox.charging.model.store.ChargingStore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * unit test case for summary services.
 */

@RunWith(MockitoJUnitRunner.class)
public class ChargingSummaryServiceImplTest {

    private static final String stationId1 = "1";
    private static final String stationId2 = "2";
    private static final String stationId3 = "3";

    @InjectMocks
    private ChargingSummaryServiceImpl chargingSummaryServiceImpl;

    @Mock
    private ChargingStore chargingStore;

    private static final Duration TWO_MIN = Duration.ofMinutes(2L);

    private LocalDateTime now;
    private LocalDateTime twoMinBefore;

    @BeforeEach
    public void init() {
        now = LocalDateTime.now();
        twoMinBefore = LocalDateTime.now().minus(TWO_MIN);
    }

    @Test
    @DisplayName("Empty summary should be returned")
    public void testEmptySummary() {

        // when
        ChargingSessionSummary chargingSessionSummary = chargingSummaryServiceImpl.getSummaryForLastMinute();

        // then
        assertThat(chargingSessionSummary, is(notNullValue()));
        assertThat(chargingSessionSummary.getStartedCount(), equalTo(0L));
        assertThat(chargingSessionSummary.getStoppedCount(), equalTo(0L));
        assertThat(chargingSessionSummary.getTotalCount(), equalTo(0L));
    }

    @Test
    @DisplayName("Only one session started in last minute then summary should return as started")
    public void testOneNewSessionsSummary() {
        // given
        long startedCount = 1L;
        long stoppedCount = 0L;
        long totalCount = 1L;

        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        final UUID uuid = UUID.randomUUID();
        final ChargingSession chargingSession = ChargingSession.builder()
                                                               .id(uuid)
                                                               .stationId(stationId1)
                                                               .startedAt(LocalDateTime.now())
                                                               .status(StatusEnum.IN_PROGRESS)
                                                                .build();
        sessions.put(UUID.randomUUID(), chargingSession);

        // when
        doReturn(sessions).when(chargingStore).getSessions();
        ChargingSessionSummary expectedResponse = new ChargingSessionSummary(totalCount, startedCount,
                stoppedCount);

        ChargingSessionSummary chargingSessionsSummaryResponse = chargingSummaryServiceImpl.getSummaryForLastMinute();

        // then
        assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
        assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
        assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
        assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
    }

    @Test
    @DisplayName("More than one sessions started and stopped in last minute then summary should return them as started")
    public void testMultipleNewSessionsSummary() {

        // given
        long startedCount = 1L;
        long stoppedCount = 2L;
        long totalCount = 3L;

        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        UUID uuid = UUID.randomUUID();

        ChargingSession firstChargingSession = ChargingSession.builder()
                                                        .id(uuid)
                                                        .stationId(stationId1)
                                                        .startedAt(LocalDateTime.now())
                                                        .status(StatusEnum.IN_PROGRESS)
                                                        .build();
        sessions.put(UUID.randomUUID(), firstChargingSession);

        uuid = UUID.randomUUID();
        ChargingSession secondChargingSession = ChargingSession.builder()
                                                                .id(uuid)
                                                                .stationId(stationId2)
                                                                .startedAt(twoMinBefore)
                                                                .stoppedAt(LocalDateTime.now())
                                                                .status(StatusEnum.FINISHED)
                                                                .build();


        sessions.put(UUID.randomUUID(), secondChargingSession);
        uuid = UUID.randomUUID();
        ChargingSession thirdChargingSession = ChargingSession.builder()
                                                              .id(uuid)
                                                              .stationId(stationId3)
                                                              .startedAt(twoMinBefore)
                                                              .stoppedAt(LocalDateTime.now())
                                                              .status(StatusEnum.FINISHED)
                                                              .build();


        sessions.put(UUID.randomUUID(), thirdChargingSession);

        // when
        doReturn(sessions).when(chargingStore).getSessions();
        ChargingSessionSummary expectedResponse = new ChargingSessionSummary(totalCount, startedCount,stoppedCount);

        ChargingSessionSummary chargingSessionSummary = chargingSummaryServiceImpl.getSummaryForLastMinute();

        // then
        assertThat(chargingSessionSummary, is(notNullValue()));
        assertThat(chargingSessionSummary.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
        assertThat(chargingSessionSummary.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
        assertThat(chargingSessionSummary.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
    }

    @Test
    @DisplayName("More than one sessions stopped in last minute then summary should return them as started")
    public void testMultipleNewStoppedSessionsSummary() {

        // given
        long startedCount = 0L;
        long stoppedCount = 2L;
        long totalCount = 2L;

        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        final UUID uuid = UUID.randomUUID();

        ChargingSession firstChargingSession = ChargingSession.builder()
                                                                .id(uuid)
                                                                .stationId(stationId1)
                                                                .startedAt(twoMinBefore)
                                                                .stoppedAt(LocalDateTime.now())
                                                                .status(StatusEnum.FINISHED)
                                                                .build();

        sessions.put(UUID.randomUUID(), firstChargingSession);

        ChargingSession secondChargingSession = ChargingSession.builder()
                .id(uuid)
                .stationId(stationId2)
                .startedAt(twoMinBefore)
                .stoppedAt(LocalDateTime.now())
                .status(StatusEnum.FINISHED)
                .build();

        sessions.put(UUID.randomUUID(), secondChargingSession);

        // when
        doReturn(sessions).when(chargingStore).getSessions();
        ChargingSessionSummary expectedResponse = new ChargingSessionSummary(totalCount, startedCount, stoppedCount);

        ChargingSessionSummary chargingSessionSummary = chargingSummaryServiceImpl.getSummaryForLastMinute();

        // then
        assertThat(chargingSessionSummary, is(notNullValue()));
        assertThat(chargingSessionSummary.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
        assertThat(chargingSessionSummary.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
        assertThat(chargingSessionSummary.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
    }

}
