package org.example.message;

import java.math.BigDecimal;

public class ProductEvent {

  private Product product;

  private ActionType actionType;

  public ProductEvent() {}

  public ProductEvent(Product product, ActionType actionType) {
    this.product = product;
    this.actionType = actionType;
  }

  public ProductEvent(
      Long productId, String productName, BigDecimal productPrice, ActionType actionType) {
    this.product = new Product(productId, productName, productPrice);
    this.actionType = actionType;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public void setActionType(ActionType actionType) {
    this.actionType = actionType;
  }
}
