package org.example.order.mapper;

import java.util.List;
import org.example.order.dto.OrderResponseDTO;
import org.example.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

  OrderResponseDTO toDto(Order order);

  List<OrderResponseDTO> toDtoList(List<Order> orders);
}
