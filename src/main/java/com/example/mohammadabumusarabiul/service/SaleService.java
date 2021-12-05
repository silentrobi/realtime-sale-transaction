package com.example.mohammadabumusarabiul.service;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;

public interface SaleService {

    void addSale(String salesAmount);

    SaleStatisticDTO getSalesStatistics();
}
