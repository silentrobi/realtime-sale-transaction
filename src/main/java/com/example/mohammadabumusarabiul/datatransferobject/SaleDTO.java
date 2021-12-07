package com.example.mohammadabumusarabiul.datatransferobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class SaleDTO extends  AbstractSaleDTO{

    @NotNull(message = "Sales amount can not be null!")
    @DecimalMin(value = "0.0", message = "Invalid sales amount")
    @JsonProperty("sales_amount")
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
