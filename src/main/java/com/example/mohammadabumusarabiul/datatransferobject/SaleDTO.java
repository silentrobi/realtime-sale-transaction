package com.example.mohammadabumusarabiul.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SaleDTO {

    @NotNull(message = "sales amount can not be null!")
    private String salesAmount;

    public String getSalesAmount() {
        return salesAmount;
    }

    public SaleDTO(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    @Override
    public String toString() {
        return "SaleDTO{" +
                "salesAmount='" + salesAmount + '\'' +
                '}';
    }
}
