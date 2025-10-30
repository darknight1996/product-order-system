package org.example.message;

import java.math.BigDecimal;

public class ProductEvent {

  private Product product;

  private ActionType actionType;

  public ProductEvent() {}

  public ProductEvent(final Product product, final ActionType actionType) {
    this.product = product;
    this.actionType = actionType;
  }

  public ProductEvent(
      final Long productId,
      final String productName,
      final BigDecimal productPrice,
      final ActionType actionType) {
    this.product = new Product(productId, productName, productPrice);
    this.actionType = actionType;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(final Product product) {
    this.product = product;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public void setActionType(final ActionType actionType) {
    this.actionType = actionType;
  }
}
