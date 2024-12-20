package org.example.catalog.mapper;

import org.example.catalog.dto.ProductAddDTO;
import org.example.catalog.dto.ProductUpdateDTO;
import org.example.catalog.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productFromProductUpdateDto(final ProductUpdateDTO productUpdateDTO);

    Product productFromProductAddDto(final ProductAddDTO productAddDTO);

    //void updateProductFromProductDto(@MappingTarget final Product product, final ProductDTO productDTO);

}
