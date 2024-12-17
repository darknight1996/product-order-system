package org.example.catalog.dto;

import java.math.BigDecimal;

public class ProductUpdateDTO {

    private String name;

    private String description;

    private BigDecimal price;

    public ProductUpdateDTO() {
    }

    public ProductUpdateDTO(final String name, final String description, final BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

}
