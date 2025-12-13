package com.tech.productservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@ImportTestcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                   "name": "Samsung Galaxy S24 Ultra",
                   "description": "The Samsung Galaxy S24 Ultra combines a powerful processor, advanced AI-driven camera features, and a stunning AMOLED display for an enhanced user experience.",
                   "price": 100000,
                   "skuCode": "SM2025LAW"
                }
                """;

        RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("name", Matchers.equalTo("Samsung Galaxy S24 Ultra"))
                .body("description", Matchers.equalTo("The Samsung Galaxy S24 Ultra combines a powerful processor, advanced AI-driven camera features, and a stunning AMOLED display for an enhanced user experience."))
                .body("skuCode", Matchers.equalTo("SM2025LAW"))
                .body("price", Matchers.equalTo(100000));
    }

}
