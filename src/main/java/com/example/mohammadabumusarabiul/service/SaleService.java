package com.example.mohammadabumusarabiul.service;

import com.example.mohammadabumusarabiul.dataaccessobject.SaleRepository;
import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    @Autowired
    public SaleService(final SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }

    public void addSale(String salesAmount){
        SaleDO saleDO = new SaleDO(UUID.randomUUID(), Long.parseLong(salesAmount));
        saleRepository.insert(saleDO);
    }

    public SaleStatisticDTO getSalesStatistics(){
        return saleRepository.calculateSalesStatistics();
    }
}
