package org.example.message;

import org.example.message.model.ActionType;
import org.example.message.model.Product;

import java.io.Serializable;

public class ProductEvent {

    private Product product;

    private ActionType actionType;

    public ProductEvent() {
    }

    public ProductEvent(final Product product, final ActionType actionType) {
        this.product = product;
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
