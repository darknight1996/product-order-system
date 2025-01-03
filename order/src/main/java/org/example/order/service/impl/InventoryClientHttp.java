package org.example.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.order.dto.OrderAddDTO;
import org.example.order.exception.InsufficientInventoryException;
import org.example.order.service.InventoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class InventoryClientHttp implements InventoryClient {

    private static final Logger logger = LoggerFactory.getLogger(InventoryClientHttp.class);

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${inventory.url}")
    private String inventoryUrl;

    public InventoryClientHttp(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void adjustInventory(OrderAddDTO orderAddDTO) {
        try {
            final String requestBody = objectMapper.writeValueAsString(orderAddDTO);
            final HttpResponse<String> response = sendAdjustInventoryRequest(requestBody);

            handleInventoryResponse(response);
        } catch (IOException | InterruptedException e) {
            logger.error("Error during inventory adjustment: {}", e.getMessage());
            throw new RuntimeException("Error during inventory adjustment: " + e.getMessage(), e);
        } catch (InsufficientInventoryException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private HttpResponse<String> sendAdjustInventoryRequest(String requestBody) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(inventoryUrl + "/api/v1/inventory/adjust"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void handleInventoryResponse(HttpResponse<String> response) {
        if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            logger.warn("Inventory conflict: {}", response.body());
            throw new InsufficientInventoryException(response.body());
        }

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            logger.warn("Inventory not found: {}", response.body());
            throw new EntityNotFoundException("Inventory not found.");
        }

        if (response.statusCode() != HttpStatus.OK.value()) {
            logger.error("Failed to adjust inventory: {}", response.body());
            throw new RuntimeException("Failed to adjust inventory: " + response.body());
        }

        logger.info("Inventory adjustment successful: {}", response.body());
    }

}
