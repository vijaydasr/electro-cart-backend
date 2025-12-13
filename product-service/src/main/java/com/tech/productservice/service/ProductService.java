package com.tech.productservice.service;

import com.tech.productservice.dto.ProductRequest;
import com.tech.productservice.dto.ProductResponse;
import com.tech.productservice.model.Product;
import com.tech.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .productName(productRequest.name())
                .productDescription(productRequest.description())
                .skuCode(productRequest.skuCode())
                .productPrice(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product created successfully");
        return new ProductResponse(product.getProductId(), product.getProductName(), product.getProductDescription(),
                product.getSkuCode(),
                product.getProductPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(product -> new ProductResponse(product.getProductId(), product.getProductName(), product.getProductDescription(), product.getSkuCode(), product.getProductPrice()))
                .toList();
    }
}
