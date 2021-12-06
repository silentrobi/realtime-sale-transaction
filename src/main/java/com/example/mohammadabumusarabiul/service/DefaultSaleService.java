package com.example.mohammadabumusarabiul.service;

import com.example.mohammadabumusarabiul.dataaccessobject.DefaultSaleRepository;
import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DefaultSaleService implements SaleService {

    private final DefaultSaleRepository saleRepository;

    @Autowired
    public DefaultSaleService(final DefaultSaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public void addSale(String salesAmount) {
        System.out.println(Thread.currentThread().getName());
        SaleDO saleDO = new SaleDO(UUID.randomUUID(), Double.parseDouble(salesAmount));
        saleRepository.upsert(saleDO.getId(), saleDO);
    }

    @Override
    public SaleStatisticDTO getSalesStatistics() {

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        return saleRepository.calculateSalesStatistics(startDateTime, endDateTime);
    }
}
