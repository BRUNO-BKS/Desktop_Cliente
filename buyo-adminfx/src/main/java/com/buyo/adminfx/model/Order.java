package com.buyo.adminfx.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private int customerId;
    private BigDecimal total;
    private LocalDateTime createdAt;

    public Order() {}

    public Order(int id, int customerId, BigDecimal total, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
