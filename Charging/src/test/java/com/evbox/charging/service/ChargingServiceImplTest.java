package com.evbox.charging.service;

import com.evbox.charging.exception.ChargingDataNotFoundException;
import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.StatusEnum;
import com.evbox.charging.model.store.ChargingStore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

/**
 * unit testcase for chargingService
 */
@RunWith(MockitoJUnitRunner.class)
public class ChargingServiceImplTest {

    private static final String stationId1 = "1";
    private static final String stationId2 = "2";
    private static final String stationId3 = "3";
    private static final String stationId4 = "4";

    private static final String id1 = "68a8a66d-6716-4c89-ad78-78efd58c7d2f";

    @InjectMocks
    private ChargingServiceImpl chargingServiceImpl;

    @Mock
    private ChargingStore chargingStore;

    private LocalDateTime now;

    @BeforeEach
    public void init() {
        now = LocalDateTime.now();
    }

    @Test
    @DisplayName("Test submit a charging session with given stationId")
    public void testSubmitChargingSession() {
        // when
        ChargingSession actualResponse = chargingServiceImpl.submitChargingSessions(stationId1);
        // then
        assertEquals(StatusEnum.IN_PROGRESS, actualResponse.getStatus());
        assertEquals(stationId1, actualResponse.getStationId());
        assertThat(actualResponse.getId(), notNullValue());

    }

    @Test
    @DisplayName("Test stop a charging session with given id")
    public void testStopChargingSession() {

        // given
        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        final UUID uuid = UUID.fromString(id1);

        ChargingSession chargingSession = ChargingSession.builder()
                                                .id(uuid)
                                                .status(StatusEnum.IN_PROGRESS)
                                                .startedAt(now)
                                                .build();

        sessions.put(uuid, chargingSession);
        // when
        doReturn(sessions).when(chargingStore).getSessions();
        ChargingSession actualResponse = chargingServiceImpl.stopChargingSessions(id1);

        // then
        assertEquals(uuid, actualResponse.getId());
        assertEquals(StatusEnum.FINISHED, actualResponse.getStatus());
    }

    @Test
    @DisplayName("Test stop a charging session with non existing id.")
    public void testStopNonExistingChargingSession() {

        // given
        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        // when
        doReturn(sessions).when(chargingStore).getSessions();

        // then
        assertThrows(ChargingDataNotFoundException.class, () -> chargingServiceImpl.stopChargingSessions(id1));

    }

    @Test
    @DisplayName("Test retrieve all charging sessions")
    public void testRetrieveAllChargingSessions() {

        // given
        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();
        UUID uuid = UUID.randomUUID();
        ChargingSession firstChargingSession = ChargingSession.builder()
                                                        .id(uuid)
                                                        .stationId(stationId1)
                                                        .status(StatusEnum.IN_PROGRESS)
                                                        .startedAt(now)
                                                        .build();

        sessions.put(uuid, firstChargingSession);
        uuid = UUID.randomUUID();
        ChargingSession secondChargingSession = ChargingSession.builder()
                                                          .id(uuid)
                                                          .stationId(stationId2)
                                                          .startedAt(now)
                                                          .status(StatusEnum.IN_PROGRESS)
                                                          .build();


        sessions.put(uuid, secondChargingSession);
        uuid = UUID.randomUUID();
        ChargingSession thirdChargingSession = ChargingSession.builder()
                                                              .id(uuid)
                                                              .stationId(stationId3)
                                                              .startedAt(now)
                                                              .status(StatusEnum.IN_PROGRESS)
                                                              .build();


        sessions.put(uuid, thirdChargingSession);
        uuid = UUID.randomUUID();
        ChargingSession finishedChargingSession = ChargingSession.builder()
                                                         .id(uuid)
                                                         .stationId(stationId4)
                                                         .status(StatusEnum.FINISHED)
                                                         .stoppedAt(now)
                                                         .build();
        sessions.put(uuid, finishedChargingSession);

        // when
        doReturn(sessions).when(chargingStore).getSessions();
        List<ChargingSession> actualResponse = chargingServiceImpl.getAllChargingSessions();

        // then
        assertEquals(actualResponse.size(), sessions.size());

    }

    @Test
    @DisplayName("Test retrieve all with no charging sessions")
    public void testRetrieveAllEmptyChargingSessions() {

        // given
        Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

        // when
        doReturn(sessions).when(chargingStore).getSessions();
        List<ChargingSession> actualResponse = chargingServiceImpl.getAllChargingSessions();

        // then
        assertEquals(actualResponse.size(), sessions.size());

    }
}
