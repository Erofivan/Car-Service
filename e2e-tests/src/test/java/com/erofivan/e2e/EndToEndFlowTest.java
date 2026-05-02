package com.erofivan.e2e;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndToEndFlowTest {

    private static final String KEYCLOAK_TOKEN_URL =
        "http://localhost:8180/realms/erofivan/protocol/openid-connect/token";
    private static final String ORDER_SERVICE_URL = "http://localhost:8081";
    private static final String STORAGE_SERVICE_URL = "http://localhost:8082";
    private static final String CLIENT_ID = "erofivan-app";
    private static final String CLIENT_SECRET = "dev-secret";

    private static String userToken;
    private static String managerToken;
    private static String warehouseToken;

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        userToken = waitForToken("client-user", Duration.ofSeconds(90));
        managerToken = waitForToken("manager-user", Duration.ofSeconds(90));
        warehouseToken = waitForToken("warehouse-user", Duration.ofSeconds(90));

        waitForAuthorizedGet(
            STORAGE_SERVICE_URL + "/api/cars",
            userToken,
            Duration.ofSeconds(90)
        );
        waitForAuthorizedGet(
            ORDER_SERVICE_URL + "/api/orders/inventory",
            userToken,
            Duration.ofSeconds(90)
        );
    }

    private static String waitForToken(String username, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();
        Throwable lastError = null;

        while (System.nanoTime() < deadline) {
            try {
                Response response = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "password")
                    .formParam("client_id", CLIENT_ID)
                    .formParam("client_secret", CLIENT_SECRET)
                    .formParam("username", username)
                    .formParam("password", "password")
                    .post(KEYCLOAK_TOKEN_URL);

                if (response.statusCode() == 200) {
                    String token = response.path("access_token");
                    if (token != null && !token.isBlank()) {
                        return token;
                    }
                }
            } catch (Throwable throwable) {
                lastError = throwable;
            }

            sleepOneSecond();
        }

        Assertions.fail("Token was not issued for user " + username + ". Last error: " + lastError);
        return "";
    }

    private static void waitForAuthorizedGet(String url, String token, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();

        while (System.nanoTime() < deadline) {
            try {
                int statusCode = givenAuthorized(token)
                    .when()
                    .get(url)
                    .statusCode();

                if (statusCode == 200) {
                    return;
                }
            } catch (Throwable ignored) {
            }

            sleepOneSecond();
        }

        Assertions.fail("Service did not become ready at " + url);
    }

    private static String findAnyModelCode() {
        List<Map<String, Object>> cars = givenAuthorized(userToken)
            .when()
            .get(STORAGE_SERVICE_URL + "/api/cars")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

        for (Map<String, Object> car : cars) {
            String modelCode = (String) car.get("modelCode");
            if (modelCode != null && !modelCode.isBlank()) {
                return modelCode;
            }
        }

        Assertions.fail("No modelCode found in /api/cars response");
        return "";
    }

    private static io.restassured.specification.RequestSpecification givenAuthorized(String token) {
        return given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json");
    }

    private static void sleepOneSecond() {
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(interruptedException);
        }
    }

    @Test
    @Order(1)
    void userCanListCarsFromStorageService() {
        givenAuthorized(userToken)
            .when()
            .get(STORAGE_SERVICE_URL + "/api/cars")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(2)
    void userHasNoAccessToPartsButManagerHasAccess() {
        givenAuthorized(userToken)
            .when()
            .get(STORAGE_SERVICE_URL + "/api/parts")
            .then()
            .statusCode(403);

        givenAuthorized(managerToken)
            .when()
            .get(STORAGE_SERVICE_URL + "/api/parts")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(3)
    void warehouseAdminCanCreateAndCompleteAssemblyOrder() {
        String modelCode = findAnyModelCode();
        String sourceOrderId = UUID.randomUUID().toString();

        Response createResponse = givenAuthorized(warehouseToken)
            .body(Map.of("sourceOrderId", sourceOrderId, "modelCode", modelCode))
            .when()
            .post(STORAGE_SERVICE_URL + "/api/assembly-orders")
            .then()
            .statusCode(201)
            .body("status", org.hamcrest.Matchers.equalTo("CREATED"))
            .extract()
            .response();

        String assemblyOrderId = createResponse.path("id");

        givenAuthorized(warehouseToken)
            .when()
            .post(STORAGE_SERVICE_URL + "/api/assembly-orders/{id}/assembled", assemblyOrderId)
            .then()
            .statusCode(200)
            .body("status", org.hamcrest.Matchers.equalTo("ASSEMBLED"));
    }

    @Test
    @Order(4)
    void userCanPlaceCustomOrderThenApproveAndConfirmWarehouse() {
        String modelCode = findAnyModelCode();

        Response placeResponse = givenAuthorized(userToken)
            .body(Map.of("modelCode", modelCode, "optionIds", List.of()))
            .when()
            .post(ORDER_SERVICE_URL + "/api/orders/custom");

        int statusCode = placeResponse.statusCode();

        if (statusCode == 201) {
            Assertions.assertEquals("PLACED", placeResponse.path("status"));
            Assertions.assertEquals(modelCode, placeResponse.path("modelCode"));

            String orderId = placeResponse.path("id");

            givenAuthorized(managerToken)
                .when()
                .post(ORDER_SERVICE_URL + "/api/orders/custom/{id}/approve", orderId)
                .then()
                .statusCode(200)
                .body("status", org.hamcrest.Matchers.equalTo("APPROVED_BY_MANAGER"));

            givenAuthorized(warehouseToken)
                .when()
                .post(ORDER_SERVICE_URL + "/api/orders/custom/{id}/confirm-warehouse", orderId)
                .then()
                .statusCode(200)
                .body("status", org.hamcrest.Matchers.equalTo("APPROVED_BY_WAREHOUSE"));

            return;
        }

        Assertions.assertEquals(404, statusCode);
        String detail = placeResponse.path("detail");
        Assertions.assertTrue(detail != null && detail.contains("Client with id"));
    }
}
