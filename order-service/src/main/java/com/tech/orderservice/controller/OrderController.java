package com.tech.orderservice.controller;

import com.tech.orderservice.dto.OrderRequest;
import com.tech.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return Map.of("message", "Your Order Placed Successfully");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRequest> getOrders() {
        return orderService.getOrders();
    }
}
