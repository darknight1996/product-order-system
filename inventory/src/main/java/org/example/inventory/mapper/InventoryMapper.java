package org.example.inventory.mapper;

import java.util.List;
import org.example.inventory.dto.InventoryResponseDTO;
import org.example.inventory.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryMapper {

  InventoryResponseDTO toInventoryResponseDTO(Inventory inventory);

  List<InventoryResponseDTO> toInventoryResponseDTOList(List<Inventory> inventories);
}
