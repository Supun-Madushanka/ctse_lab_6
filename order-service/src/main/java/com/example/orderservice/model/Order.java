package com.example.orderservice.model;

public class Order {

    private String orderId;
    private String item;
    private int quantity;

    public Order() {}

    public Order(String orderId, String item, int quantity) {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', item='" + item + "', quantity=" + quantity + "}";
    }
}
