package org.example.catalog.mapper;

import org.example.catalog.dto.ProductAddDTO;
import org.example.catalog.dto.ProductUpdateDTO;
import org.example.catalog.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product productFromProductUpdateDto(ProductUpdateDTO productUpdateDTO);

  Product productFromProductAddDto(ProductAddDTO productAddDTO);
}
