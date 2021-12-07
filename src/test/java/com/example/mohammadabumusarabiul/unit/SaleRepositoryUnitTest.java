package com.example.mohammadabumusarabiul.unit;

import com.example.mohammadabumusarabiul.dataaccessobject.DefaultSaleRepository;
import com.example.mohammadabumusarabiul.dataaccessobject.SaleRepository;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.example.mohammadabumusarabiul.util.DateTimeHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SaleRepositoryUnitTest {

    private final ConcurrentHashMap<UUID, SaleDO> storage = new ConcurrentHashMap<>();
    private final SaleRepository saleRepository = new DefaultSaleRepository(storage, new DateTimeHelper());

    @Before
    public void clearSaleStorage() {
        storage.clear();
    }

    @Test
    public void shouldInsertNewSale() {
        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 100.0);

        saleRepository.upsert(saleDO.getId(), saleDO);

        var entry = saleRepository.findById(saleDO.getId());

        Assert.assertEquals(saleDO.getId(), entry.getId());
        Assert.assertEquals(saleDO.getSalesAmount(), entry.getSalesAmount());
    }

    @Test
    public void shouldUpdateSale() {

        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 100.0);
        storage.put(saleDO.getId(), saleDO);

        saleRepository.upsert(saleDO.getId(), new SaleDO(saleDO.getId(), 200.0));

        var entry = saleRepository.findById(saleDO.getId());

        Assert.assertEquals(Double.valueOf(200.0), entry.getSalesAmount());
    }

    @Test
    public void shouldDeleteASale() {

        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 100.0);
        storage.put(saleDO.getId(), saleDO);

        saleRepository.delete(saleDO.getId());

        var entry = saleRepository.findById(saleDO.getId());

        Assert.assertEquals(null, entry);
    }

    @Test
    public void shouldCalculateSalesStatistics() throws InterruptedException {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 250.0));
        sales.add(new SaleDO(UUID.randomUUID(), 175.0));

        for(SaleDO saleDO : sales){
            storage.put(saleDO.getId(),saleDO);
        }

        Thread.sleep(10);

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        var saleStatisticDTO = saleRepository.calculateSalesStatistics(startDateTime, endDateTime);

        Assert.assertEquals(Double.toString(575.0), saleStatisticDTO.getTotalSalesAmount());
        Assert.assertEquals(Double.toString(143.75), saleStatisticDTO.getAverageAmountPerOrder());
    }
}
