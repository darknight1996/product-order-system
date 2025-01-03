package org.example.order.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.order.service.CatalogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CatalogClientHttp implements CatalogClient {

    private static final Logger logger = LoggerFactory.getLogger(CatalogClientHttp.class);

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${catalog.url}")
    private String catalogUrl;

    public CatalogClientHttp(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public BigDecimal getProductPrice(Long productId) {
        try {
            final HttpResponse<String> response = sendGetProductRequest(productId);

            handleCatalogResponse(response, productId);

            final JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.get("price").decimalValue();
        } catch (IOException | InterruptedException e) {
            logger.error("Error during fetching product price for ID: {}: {}", productId, e.getMessage());
            throw new RuntimeException("Error fetching product price: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error for product ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private HttpResponse<String> sendGetProductRequest(Long productId) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(catalogUrl + "/api/v1/product/" + productId))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void handleCatalogResponse(HttpResponse<String> response, Long productId) {
        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            logger.warn("Product not found in catalog for ID: {}", productId);
            throw new EntityNotFoundException("Product not found in catalog for ID: " + productId);
        }

        if (response.statusCode() != HttpStatus.OK.value()) {
            logger.error("Failed to fetch product details for ID: {}: {}", productId, response.body());
            throw new RuntimeException("Failed to fetch product details: " + response.body());
        }

        logger.info("Successfully fetched product details for ID: {}: {}", productId, response.body());
    }

}