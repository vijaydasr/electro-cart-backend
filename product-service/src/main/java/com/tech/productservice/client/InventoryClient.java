package com.tech.productservice.client;

import com.tech.productservice.client.model.InventoryResponse;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@Slf4j
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    // Refer RestClient documentation http://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
    @GetExchange("/api/inventory/all")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    List<InventoryResponse> getAllInventories();

    default boolean fallbackMethod(Throwable throwable) {
        log.info("Cannot get inventory due to: " + throwable.getMessage());
        return false;
    }
}
