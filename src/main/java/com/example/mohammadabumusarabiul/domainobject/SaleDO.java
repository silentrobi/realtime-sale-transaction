package com.example.mohammadabumusarabiul.domainobject;

import java.time.ZonedDateTime;
import java.util.UUID;

public class SaleDO {

    private UUID id;
    private Long salesAmount;
    private ZonedDateTime date =  ZonedDateTime.now();

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
