package com.erofivan;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StorageServiceIT extends AbstractStorageServiceIT {

    @Test
    void listCarsReturnsSeededData() throws Exception {
        mockMvc.perform(get("/api/cars").with(userJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    void getCarAvailabilityReturnsCorrectShape() throws Exception {
        MvcResult list = mockMvc.perform(get("/api/cars").with(userJwt()))
            .andReturn();

        String firstId = com.jayway.jsonpath.JsonPath.read(
            list.getResponse().getContentAsString(), "$[0].id");

        mockMvc.perform(get("/api/cars/{id}/availability", firstId).with(userJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(firstId))
            .andExpect(jsonPath("$.available").isBoolean())
            .andExpect(jsonPath("$.testDriveEnabled").isBoolean());
    }

    @Test
    void getConfigurationReturnsCorrectModelCode() throws Exception {
        mockMvc.perform(get("/api/configurations/BMW-320I").with(userJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modelCode").value("BMW-320I"));
    }

    @Test
    void listPartsRequiresManagerRole() throws Exception {
        mockMvc.perform(get("/api/parts").with(userJwt()))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/parts").with(managerJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createAndMarkAssembledAssemblyOrder() throws Exception {
        MvcResult create = mockMvc.perform(post("/api/assembly-orders")
                .with(warehouseJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"sourceOrderId": "11111111-0000-0000-0000-000000000001",
                     "modelCode": "BMW-320I"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andReturn();

        String assemblyId = com.jayway.jsonpath.JsonPath.read(
            create.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/assembly-orders/{id}/assembled", assemblyId)
                .with(warehouseJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ASSEMBLED"));
    }

    @Test
    void createAndMarkFailedAssemblyOrder() throws Exception {
        MvcResult create = mockMvc.perform(post("/api/assembly-orders")
                .with(warehouseJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"sourceOrderId": "22222222-0000-0000-0000-000000000001",
                     "modelCode": "BMW-320I"}
                    """))
            .andExpect(status().isCreated())
            .andReturn();

        String assemblyId = com.jayway.jsonpath.JsonPath.read(
            create.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/assembly-orders/{id}/failed", assemblyId)
                .with(warehouseJwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("FAIL"));
    }

    private RequestPostProcessor userJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }

    private RequestPostProcessor managerJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_MANAGER"));
    }

    private RequestPostProcessor warehouseJwt() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_WAREHOUSE_ADMIN"));
    }
}
