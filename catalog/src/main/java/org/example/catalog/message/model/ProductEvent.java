package org.example.catalog.message.model;

public class ProductEvent {

    private Product product;

    private ActionType actionType;

    public ProductEvent() {
    }

    public ProductEvent(final org.example.catalog.entity.Product product, final ActionType actionType) {
        this.product = new Product();
        this.product.setId(product.getId());
        this.product.setName(product.getName());
        this.product.setPrice(product.getPrice());

        this.actionType = actionType;
    }

    public ProductEvent(final Product product, final ActionType actionType) {
        this.product = product;
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
