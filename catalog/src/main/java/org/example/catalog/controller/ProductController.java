package org.example.catalog.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.catalog.dto.ProductAddDTO;
import org.example.catalog.dto.ProductResponseDTO;
import org.example.catalog.dto.ProductUpdateDTO;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  public ResponseEntity<List<ProductResponseDTO>> getAll() {
    List<Product> products = productService.getAll();
    List<ProductResponseDTO> response =
        products.stream().map(productMapper::toProductResponseDTO).toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
    Product product = productService.getById(id);

    return ResponseEntity.ok(productMapper.toProductResponseDTO(product));
  }

  @PostMapping
  public ResponseEntity<ProductResponseDTO> add(@Valid @RequestBody ProductAddDTO productAddDTO) {
    Product product = productMapper.fromProductAddDto(productAddDTO);
    Product savedProduct = productService.add(product);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(productMapper.toProductResponseDTO(savedProduct));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
    Product product = productMapper.fromProductUpdateDto(productUpdateDTO);

    product.setId(id);

    Product updatedProduct = productService.update(product);

    return ResponseEntity.ok(productMapper.toProductResponseDTO(updatedProduct));
  }
}
