package com.tech.orderservice.client;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Slf4j
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    // Removed FeignClient annotation and replaced with Spring Web Service annotation ->
    // Refer RestClient documentation http://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
    @GetExchange("/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable throwable) {
        log.info("Cannot get inventory for SKU: " + skuCode + " due to: " + throwable.getMessage());
        return false;
    }
}
