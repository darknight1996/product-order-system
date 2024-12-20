package org.example.catalog.controller;

import jakarta.validation.Valid;
import org.example.catalog.dto.ProductAddDTO;
import org.example.catalog.dto.ProductUpdateDTO;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(final ProductService productService, final ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable final Long id) {
        final Product product = productService.getById(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> add(@RequestBody final ProductAddDTO productAddDTO) {
        final Product product = productMapper.productFromProductAddDto(productAddDTO);

        final Product savedProduct = productService.add(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        productService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Product> update(@Valid @RequestBody final ProductUpdateDTO productUpdateDTO) {
        final Product product = productMapper.productFromProductUpdateDto(productUpdateDTO);

        final Product updatedProduct = productService.update(product);

        return ResponseEntity.ok().body(updatedProduct);
    }

}
