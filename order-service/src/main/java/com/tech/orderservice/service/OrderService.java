package com.tech.orderservice.service;

import com.tech.orderservice.client.InventoryClient;
import com.tech.orderservice.dto.OrderRequest;
import com.tech.orderservice.event.OrderEvent;
import com.tech.orderservice.model.Order;
import com.tech.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (!inStock) {
            throw new RuntimeException("Product with SKU code: " + orderRequest.skuCode() + " is not in stock");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderId());
        order.setPrice(orderRequest.price());
        order.setSkuCode(orderRequest.skuCode());
        order.setQuantity(orderRequest.quantity());
        order.setUsername(orderRequest.userDetails().username());
        order.setEmail(orderRequest.userDetails().emailId());

        orderRepository.save(order);

        // Send message to Kafka topic
        OrderEvent orderEvent = new OrderEvent(order.getOrderNumber(), orderRequest.userDetails().emailId());
        log.info("Sending order event to Kafka: {}", orderEvent);
        kafkaTemplate.send("order-placed", orderEvent);
        log.info("Order event sent to Kafka successfully, {}", orderEvent);
    }

    public List<OrderRequest> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> new OrderRequest(
                order.getId(),
                order.getOrderNumber(),
                order.getSkuCode(),
                order.getPrice(),
                order.getQuantity(),
                new OrderRequest.UserDetails(order.getEmail(), null, null, order.getUsername())
        )).toList();
    }

    public static String generateOrderId() {
        // Generate UUID
        UUID uuid = UUID.randomUUID();

        // Take the first 64 bits (most significant bits)
        long mostSigBits = uuid.getMostSignificantBits();

        // Convert to positive number
        long positive = mostSigBits & Long.MAX_VALUE;

        // Encode in Base36 (using digits 0-9 and letters A-Z)
        String base36 = Long.toString(positive, 36).toUpperCase();

        // You can adjust the length here, e.g., take the first 7 characters
        if (base36.length() > 7) {
            base36 = base36.substring(0, 7);
        } else if (base36.length() < 7) {
            base36 = String.format("%7s", base36).replace(' ', '0');
        }

        return base36;
    }
}
