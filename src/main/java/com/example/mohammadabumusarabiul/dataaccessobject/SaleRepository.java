package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;

import java.time.LocalDateTime;

public interface SaleRepository {
    SaleStatisticDTO calculateSalesStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
