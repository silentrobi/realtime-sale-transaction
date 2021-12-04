package com.example.mohammadabumusarabiul.domainobject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class SaleDO {

    private UUID id;
    private Long salesAmount;
    private LocalDateTime date =  LocalDateTime.now();

    public SaleDO(UUID id, Long salesAmount) {
        this.id = id;
        this.salesAmount = salesAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Long salesAmount) {
        this.salesAmount = salesAmount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
