package com.erofivan;

import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderServiceIT extends AbstractOrderServiceIT {

    private static final UUID CLIENT_ID  = UUID.fromString("50000000-0000-0000-0000-000000000001");
    private static final UUID MANAGER_ID = UUID.fromString("50000000-0000-0000-0000-000000000002");
    private static final UUID CAR_ID = UUID.randomUUID();

    @Test
    void getInventoryOrdersReturns200() throws Exception {
        mockMvc.perform(get("/api/orders/inventory").with(userJwt(CLIENT_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void placeInventoryOrderReturns201() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));

        mockMvc.perform(post("/api/orders/inventory")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("PLACED"))
            .andExpect(jsonPath("$.carId").value(CAR_ID.toString()));
    }

    @Test
    void placeInventoryOrderReturns400WhenCarNotAvailable() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, false, false));

        mockMvc.perform(post("/api/orders/inventory")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void placeAndApproveInventoryOrder() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));

        MvcResult place = mockMvc.perform(post("/api/orders/inventory")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isCreated())
            .andReturn();

        String orderId = com.jayway.jsonpath.JsonPath.read(
            place.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/orders/inventory/{id}/approve", orderId)
                .with(managerJwt(MANAGER_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED_BY_MANAGER"));
    }

    @Test
    void placeInventoryOrderAndCancel() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));

        MvcResult place = mockMvc.perform(post("/api/orders/inventory")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isCreated())
            .andReturn();

        String orderId = com.jayway.jsonpath.JsonPath.read(
            place.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/orders/inventory/{id}/cancel", orderId)
                .with(userJwt(CLIENT_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void scheduleTestDriveSuccess() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, true));

        mockMvc.perform(post("/api/test-drives")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s", "startsAt": "2099-12-31T10:00:00"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.carId").value(CAR_ID.toString()));
    }

    @Test
    void scheduleTestDriveReturns400WhenDisabled() throws Exception {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));

        mockMvc.perform(post("/api/test-drives")
                .with(userJwt(CLIENT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"carId": "%s", "startsAt": "2099-12-31T10:00:00"}
                    """.formatted(CAR_ID)))
            .andExpect(status().isBadRequest());
    }

    private RequestPostProcessor userJwt(UUID userId) {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))
            .jwt(j -> j.subject(userId.toString()));
    }

    private RequestPostProcessor managerJwt(UUID userId) {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_MANAGER"))
            .jwt(j -> j.subject(userId.toString()));
    }
}
