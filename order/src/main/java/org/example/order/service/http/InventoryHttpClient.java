package org.example.order.service.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.example.order.dto.OrderAddDTO;
import org.example.order.exception.InsufficientInventoryException;
import org.example.order.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class InventoryHttpClient implements InventoryService {

  private static final Logger logger = LoggerFactory.getLogger(InventoryHttpClient.class);

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  @Value("${inventory.url}")
  private String inventoryUrl;

  public InventoryHttpClient(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.httpClient = HttpClient.newHttpClient();
  }

  @Override
  public void adjustInventory(OrderAddDTO orderAddDTO) {
    try {
      String requestBody = objectMapper.writeValueAsString(orderAddDTO);
      HttpResponse<String> response = sendAdjustInventoryRequest(requestBody);

      validateResponse(response);
    } catch (IOException | InterruptedException e) {
      logger.error("Error adjusting inventory: {}", e.getMessage());
      throw new RuntimeException("Error adjusting inventory", e);
    }
  }

  private HttpResponse<String> sendAdjustInventoryRequest(String requestBody)
      throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(inventoryUrl + "/api/v1/inventory/adjust"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private void validateResponse(HttpResponse<String> response) {
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

    logger.info("Inventory adjusted successfully.");
  }
}
