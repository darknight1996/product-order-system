package org.example.catalog.dto;

import java.math.BigDecimal;

public class ProductAddDTO {

    private String name;

    private String description;

    private BigDecimal price;

    public ProductAddDTO() {
    }

    public ProductAddDTO(final String name, final String description, final BigDecimal price) {
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
