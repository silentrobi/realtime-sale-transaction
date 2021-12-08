package com.example.mohammadabumusarabiul.unit;

import com.example.mohammadabumusarabiul.dataaccessobject.DefaultSaleRepository;
import com.example.mohammadabumusarabiul.dataaccessobject.SaleRepository;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.example.mohammadabumusarabiul.util.DateTimeHelper;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Assertions.assertEquals(saleDO.getId(), entry.getId());
        Assertions.assertEquals(saleDO.getSalesAmount(), entry.getSalesAmount());
    }

    @Test
    public void shouldUpdateSale() {
        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 100.0);
        storage.put(saleDO.getId(), saleDO);

        saleRepository.upsert(saleDO.getId(), new SaleDO(saleDO.getId(), 200.0));

        var entry = saleRepository.findById(saleDO.getId());

        Assertions.assertEquals(Double.valueOf(200.0), entry.getSalesAmount());
    }

    @Test
    public void shouldDeleteASale() {
        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 100.0);
        storage.put(saleDO.getId(), saleDO);

        saleRepository.delete(saleDO.getId());

        var entry = saleRepository.findById(saleDO.getId());

        Assertions.assertNull(entry);
    }

    @Test
    public void shouldCalculateSalesStatistics() throws InterruptedException {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 250.0));
        sales.add(new SaleDO(UUID.randomUUID(), 175.0));

        for (SaleDO saleDO : sales) {
            storage.put(saleDO.getId(), saleDO);
        }

        Thread.sleep(10);

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        var saleStatisticDTO = saleRepository.calculateSalesStatistics(startDateTime, endDateTime);

        Assertions.assertEquals("575.00", saleStatisticDTO.getTotalSalesAmount());
        Assertions.assertEquals("143.75", saleStatisticDTO.getAverageAmountPerOrder());
    }

    @Test
    public void shouldCalculateSalesStatisticsOfAllOrdersExceptLastOne_saleDateTimeWithinStartDateTimeCheck() throws InterruptedException {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 210.0));
        for (SaleDO saleDO : sales) {
            storage.put(saleDO.getId(), saleDO);
        }

        Thread.sleep(10);

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        Thread.sleep(10);

        SaleDO saleDO = new SaleDO(UUID.randomUUID(), 175.0);
        storage.put(saleDO.getId(), saleDO);

        var saleStatisticDTO = saleRepository.calculateSalesStatistics(startDateTime, endDateTime);

        Assertions.assertEquals("410.00", saleStatisticDTO.getTotalSalesAmount());
        Assertions.assertEquals("102.50", saleStatisticDTO.getAverageAmountPerOrder());
    }

    @Test
    public void shouldCalculateSalesStatisticsOfAllOrdersExceptFirstOne_saleDateTimeWithinEndDateTimeCheck() throws InterruptedException {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));

        LocalDateTime dateTime = sales.get(0).getDate();
        sales.get(0).setDate(dateTime.minusMinutes(1));

        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 210.0));
        for (SaleDO saleDO : sales) {
            storage.put(saleDO.getId(), saleDO);
        }

        Thread.sleep(10);

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        var saleStatisticDTO = saleRepository.calculateSalesStatistics(startDateTime, endDateTime);

        Assertions.assertEquals("310.00", saleStatisticDTO.getTotalSalesAmount());
        Assertions.assertEquals("103.33", saleStatisticDTO.getAverageAmountPerOrder());
    }

    @Test
    public void shouldDeletedOrdersThatFall_InDeletedRange() throws InterruptedException {
        List<SaleDO> sales = new ArrayList<>();
        sales.add(new SaleDO(UUID.randomUUID(), 100.0));

        LocalDateTime dateTime = sales.get(0).getDate();
        sales.get(0).setDate(dateTime.minusMinutes(1));

        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 50.0));
        sales.add(new SaleDO(UUID.randomUUID(), 210.0));

        for (SaleDO saleDO : sales) {
            storage.put(saleDO.getId(), saleDO);
        }

        Thread.sleep(10);

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        saleRepository.calculateSalesStatistics(startDateTime, endDateTime);

        Thread.sleep(100);

        SaleDO saleDO = storage.get(sales.get(0).getId());

        Assertions.assertNull(saleDO);

        Assertions.assertEquals(3, storage.size());
    }
}
