package com.erofivan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class OrderFlowIT {

    private static final String CLIENT_ID = "50000000-0000-0000-0000-000000000001";
    private static final String MANAGER_ID = "50000000-0000-0000-0000-000000000002";
    private static final String WAREHOUSE_ID = "50000000-0000-0000-0000-000000000003";
    private static final String ADMIN_ID = "50000000-0000-0000-0000-000000000004";
    private static final String CAR_ID = "40000000-0000-0000-0000-000000000001";
    private static final String CAR_ID_NO_TD = "40000000-0000-0000-0000-000000000003";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
            () -> "http://localhost:8180/realms/erofivan");
    }

    @Test
    void unauthenticatedRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/cars"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void listCars() throws Exception {
        mockMvc.perform(get("/api/cars")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void listCarsByBrand() throws Exception {
        mockMvc.perform(get("/api/cars").param("brandCode", "BMW")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getConfiguration() throws Exception {
        mockMvc.perform(get("/api/configurations/BMW-320I")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modelCode", is("BMW-320I")))
            .andExpect(jsonPath("$.basePrice", is(4000000)))
            .andExpect(jsonPath("$.selectedOptions", notNullValue()));
    }

    @Test
    void getConfigurationNotFound() throws Exception {
        mockMvc.perform(get("/api/configurations/NONEXISTENT")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isNotFound());
    }

    @Test
    void listPartsAsManager() throws Exception {
        mockMvc.perform(get("/api/parts")
                .with(jwt().jwt(j -> j
                    .subject(MANAGER_ID)
                    .claim("realm_access", Map.of("roles", List.of("MANAGER"))))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void listPartsAsUserForbidden() throws Exception {
        mockMvc.perform(get("/api/parts")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
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
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
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
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
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
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
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
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isConflict());
    }

    @Test
    void userCannotApproveOrder() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "carId", CAR_ID
        ));

        String response = mockMvc.perform(post("/api/orders/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(post("/api/orders/inventory/" + orderId + "/approve")
                .with(jwt().jwt(j -> j
                    .subject(CLIENT_ID)
                    .claim("realm_access", Map.of("roles", List.of("USER"))))))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessEverything() throws Exception {
        mockMvc.perform(get("/api/cars")
                .with(jwt().jwt(j -> j
                    .subject(ADMIN_ID)
                    .claim("realm_access", Map.of("roles", List.of("ADMIN"))))))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/parts")
                .with(jwt().jwt(j -> j
                    .subject(ADMIN_ID)
                    .claim("realm_access", Map.of("roles", List.of("ADMIN"))))))
            .andExpect(status().isOk());
    }
}
