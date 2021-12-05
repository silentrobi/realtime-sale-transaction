package com.example.mohammadabumusarabiul.domainobject;

import java.time.LocalDateTime;

import java.util.UUID;

public class SaleDO {

    private UUID id;
    private Double salesAmount;
    private LocalDateTime date =  LocalDateTime.now();

    public SaleDO(UUID id, Double salesAmount) {
        this.id = id;
        this.salesAmount = salesAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
