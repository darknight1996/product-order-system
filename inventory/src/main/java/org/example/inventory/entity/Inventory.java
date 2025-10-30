package org.example.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long productId;

  private String productName;

  private BigDecimal productPrice;

  private Integer quantity;

  public Inventory() {}

  public Inventory(
      final Long id,
      final Long productId,
      final String productName,
      final BigDecimal productPrice,
      final Integer quantity) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.quantity = quantity;
  }

  public Inventory(
      final Long productId,
      final String productName,
      final BigDecimal productPrice,
      final Integer quantity) {
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(final Long productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(final String productName) {
    this.productName = productName;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(final BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(final Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Inventory inventory)) return false;
    return Objects.equals(id, inventory.id)
        && Objects.equals(productId, inventory.productId)
        && Objects.equals(productName, inventory.productName)
        && Objects.equals(productPrice, inventory.productPrice)
        && Objects.equals(quantity, inventory.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, productName, productPrice, quantity);
  }
}
