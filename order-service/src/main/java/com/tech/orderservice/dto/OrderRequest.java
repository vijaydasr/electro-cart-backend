package com.tech.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity, UserDetails userDetails) {

    public record UserDetails(String emailId, String firstName, String lastName, String username) {}
}
