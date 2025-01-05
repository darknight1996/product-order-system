package org.example.order.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.order.service.CatalogService;
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
public class CatalogHttpClient implements CatalogService {

    private static final Logger logger = LoggerFactory.getLogger(CatalogHttpClient.class);

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${catalog.url}")
    private String catalogUrl;

    public CatalogHttpClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public BigDecimal getProductPrice(Long productId) {
        try {
            HttpResponse<String> response = sendGetProductRequest(productId);
            validateResponse(response, productId);

            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.get("price").decimalValue();
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching product price for ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Error fetching product price", e);
        }
    }

    private HttpResponse<String> sendGetProductRequest(Long productId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(catalogUrl + "/api/v1/product/" + productId))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void validateResponse(HttpResponse<String> response, Long productId) {
        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            logger.warn("Product not found for ID: {}", productId);
            throw new EntityNotFoundException("Product not found for ID: " + productId);
        }

        if (response.statusCode() != HttpStatus.OK.value()) {
            logger.error("Error fetching product details for ID {}: {}", productId, response.body());
            throw new RuntimeException("Error fetching product details: " + response.body());
        }

        logger.info("Product details fetched successfully for ID {}", productId);
    }

}
