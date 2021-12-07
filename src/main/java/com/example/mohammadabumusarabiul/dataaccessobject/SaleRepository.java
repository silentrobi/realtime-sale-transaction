package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SaleRepository extends CrudRepository<SaleDO, UUID>{
    SaleStatisticDTO calculateSalesStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
