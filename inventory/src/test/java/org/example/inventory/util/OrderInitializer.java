package org.example.inventory.util;

import org.example.inventory.dto.OrderDTO;

public class OrderInitializer {

    public static OrderDTO createOrderDTO() {
        return new OrderDTO(1L, 10);
    }

}
