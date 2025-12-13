package com.tech.inventoryservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportTestcontainers
@Import(TestcontainersConfiguration.class)
class InventoryServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

    @Autowired
    private MockMvc mockMvc;

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldReadInventory() throws Exception {

        mockMvc.perform(
                        get("/api/inventory")
                                .param("skuCode", "iphone_15")
                                .param("quantity", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(
                        get("/api/inventory")
                                .param("skuCode", "iphone_15")
                                .param("quantity", "1000")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
