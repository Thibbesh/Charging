package com.evbox.charging.controller;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionSummary;
import com.evbox.charging.model.StatusEnum;
import com.evbox.charging.service.ChargingService;
import com.evbox.charging.service.ChargingSummaryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * unit testcase for chargingController
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChargingControllerTest {

    private static final String stationId1 = "ABC-123";
    private static final String stationId2 = "ABC-456";
    private static final String stationIdBlank = "";


    private static final String BASE_URI = "/chargingSessions";

    private MockMvc mockMvc;

    @MockBean
    private ChargingService chargingService;

    @MockBean
    private ChargingSummaryServiceImpl chargingSummaryService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCreate_withCorrectStationId() throws Exception {
        mockMvc.perform(post(BASE_URI).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content("{\"stationId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }


    @Test
    @DisplayName("PUT /chargingSessions - Success")
    public void testStopChargingSession() throws Exception {

        // given
        String id = stationId1;

        // when
        UUID uuid = UUID.randomUUID();
        // when
        ChargingSession chargingSession = ChargingSession.builder()
                                                            .id(uuid)
                                                            .stationId(stationId1)
                                                            .startedAt(LocalDateTime.now())
                                                            .status(StatusEnum.FINISHED)
                                                            .build();

        doReturn(chargingSession).when(chargingService).stopChargingSessions(id);

        // Execute the PUT request
        mockMvc.perform(put("/chargingSessions/{id}", id).contentType(MediaType.APPLICATION_JSON))

                // Validate the response code
                .andExpect(status().isOk())

                // Validate the response
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession.getStationId())))
                .andExpect(jsonPath("$.status", equalTo("FINISHED")));

    }
    @Test
    @DisplayName("GET /chargingSessions - Success")
    public void testGetChargingSessions() throws Exception {

        // given
        List<ChargingSession> chargingSessions = new ArrayList<>();

        UUID uuid = UUID.randomUUID();
        ChargingSession firstChargingSession = ChargingSession.builder()
                                                                .id(uuid)
                                                                .stationId(stationId1)
                                                                .startedAt(LocalDateTime.now())
                                                                .status(StatusEnum.IN_PROGRESS)
                                                                .build();
        uuid = UUID.randomUUID();
        ChargingSession secondChargingSession = ChargingSession.builder()
                                                                .id(uuid)
                                                                .stationId(stationId2)
                                                                .startedAt(LocalDateTime.now())
                                                                .stoppedAt(LocalDateTime.now())
                                                                .status(StatusEnum.FINISHED)
                                                                .build();
        chargingSessions.add(firstChargingSession);
        chargingSessions.add(secondChargingSession);
        doReturn(chargingSessions).when(chargingService).getAllChargingSessions();

        // Execute the GET request
        mockMvc.perform(get("/chargingSessions"))

                // Validate the response code and content type
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(chargingSessions.get(0).getId().toString())))
                .andExpect(jsonPath("$[0].stationId", equalTo(chargingSessions.get(0).getStationId())))

                .andExpect(jsonPath("$[0].status", equalTo("IN_PROGRESS")))
                .andExpect(jsonPath("$[1].id", equalTo(chargingSessions.get(1).getId().toString())))
                .andExpect(jsonPath("$[1].stationId", equalTo(chargingSessions.get(1).getStationId())))

                .andExpect(jsonPath("$[1].status", equalTo(chargingSessions.get(1).getStatus().toString().toUpperCase())));

    }

    @Test
    @DisplayName("GET /chargingSessions/summary - Success")
    public void testGetChargingSessionsSummary() throws Exception {

        // given
        ChargingSessionSummary chargingSessionsSummary = new ChargingSessionSummary(3L, 1L, 2L);

        // when
        doReturn(chargingSessionsSummary).when(chargingSummaryService).getSummaryForLastMinute();

        // Execute the GET request
        mockMvc.perform(get("/chargingSessions/summary"))

                // Validate the response code and content type
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                // Validate the returned fields
                .andExpect(jsonPath("$.totalCount", equalTo((int) chargingSessionsSummary.getTotalCount())))
                .andExpect(jsonPath("$.startedCount", equalTo((int) chargingSessionsSummary.getStartedCount())))
                .andExpect(jsonPath("$.stoppedCount", equalTo((int) chargingSessionsSummary.getStoppedCount())));

    }

     static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
