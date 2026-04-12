package com.erofivan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class OrderFlowIT extends AbstractIntegrationTest {

    private static final String CLIENT_ID = "50000000-0000-0000-0000-000000000001";
    private static final String MANAGER_ID = "50000000-0000-0000-0000-000000000002";
    private static final String ADMIN_ID = "50000000-0000-0000-0000-000000000004";
    private static final String CAR_ID = "40000000-0000-0000-0000-000000000001";
    private static final String CAR_ID_NO_TD = "40000000-0000-0000-0000-000000000003";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static JwtRequestPostProcessor asRole(String subject, String role) {
        return jwt().jwt(j -> j.subject(subject))
            .authorities(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Test
    void unauthenticatedRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/cars"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void listCars() throws Exception {
        mockMvc.perform(get("/api/cars")
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void listCarsByBrand() throws Exception {
        mockMvc.perform(get("/api/cars").param("brandCode", "BMW")
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getConfiguration() throws Exception {
        mockMvc.perform(get("/api/configurations/BMW-320I")
                .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modelCode", is("BMW-320I")))
            .andExpect(jsonPath("$.basePrice", is(4000000)))
            .andExpect(jsonPath("$.selectedOptions", notNullValue()));
    }

    @Test
    void getConfigurationNotFound() throws Exception {
        mockMvc.perform(get("/api/configurations/NONEXISTENT")
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isNotFound());
    }

    @Test
    void listPartsAsManager() throws Exception {
        mockMvc.perform(get("/api/parts")
            .with(asRole(MANAGER_ID, "MANAGER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void listPartsAsUserForbidden() throws Exception {
        mockMvc.perform(get("/api/parts")
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isForbidden());
    }

    @Test
    void placeInventoryOrder() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "carId", "40000000-0000-0000-0000-000000000002"
        ));

        mockMvc.perform(post("/api/orders/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.status", is("PLACED")));
    }

    @Test
    void placeCustomOrder() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "modelCode", "BMW-320I"
        ));

        mockMvc.perform(post("/api/orders/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.modelCode", is("BMW-320I")))
            .andExpect(jsonPath("$.status", is("PLACED")));
    }

    @Test
    void scheduleTestDrive() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "carId", CAR_ID,
            "startsAt", LocalDateTime.now().plusDays(3).toString()
        ));

        mockMvc.perform(post("/api/test-drives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.clientId", is(CLIENT_ID)));
    }

    @Test
    void scheduleTestDriveDisabledCar() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "carId", CAR_ID_NO_TD,
            "startsAt", LocalDateTime.now().plusDays(1).toString()
        ));

        mockMvc.perform(post("/api/test-drives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isBadRequest());
    }

    @Test
    void userCannotApproveOrder() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "carId", CAR_ID
        ));

        String response = mockMvc.perform(post("/api/orders/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(post("/api/orders/inventory/" + orderId + "/approve")
            .with(asRole(CLIENT_ID, "USER")))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessEverything() throws Exception {
        mockMvc.perform(get("/api/cars")
                .with(asRole(ADMIN_ID, "ADMIN")))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/parts")
                .with(asRole(ADMIN_ID, "ADMIN")))
            .andExpect(status().isOk());
    }
}
