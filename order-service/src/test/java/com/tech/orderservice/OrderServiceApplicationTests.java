package com.tech.orderservice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tech.orderservice.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.3.0");

    @LocalServerPort
    private Integer port;

    static int wireMockPort;

    @BeforeAll
    static void setup(WireMockRuntimeInfo wmInfo) {
        wireMockPort = wmInfo.getHttpPort();
    }


    // Step 2: Dynamic property source MUST take only one argument
    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("inventory.service.url",
                () -> "http://localhost:" + wireMockPort);
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mysqlContainer.start();
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                  "skuCode": "samsung_s24_ultra",
                  "price": 100000,
                  "quantity": 10,
                  "userDetails": {
                    "emailId": "test@test.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "username": "testUser"
                  }
                }
                """;

        InventoryClientStub.stubInventoryCall("samsung_s24_ultra", 10);

        var responseBodyString = RestAssured.given().contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        assertThat(responseBodyString.equals("Order Placed Successfully"));
    }
}
