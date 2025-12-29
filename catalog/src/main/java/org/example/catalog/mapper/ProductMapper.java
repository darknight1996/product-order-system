package org.example.catalog.mapper;

import org.example.catalog.dto.ProductAddDTO;
import org.example.catalog.dto.ProductResponseDTO;
import org.example.catalog.dto.ProductUpdateDTO;
import org.example.catalog.entity.Product;
import org.mapstruct.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

  Product fromProductUpdateDto(ProductUpdateDTO productUpdateDTO);

  Product fromProductAddDto(ProductAddDTO productAddDTO);

  ProductResponseDTO toProductResponseDTO(Product product);

  void updateProductFromProduct(Product source, @MappingTarget Product target);
}
