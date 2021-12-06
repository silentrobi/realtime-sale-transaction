package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.example.mohammadabumusarabiul.util.DateTimeHelper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SaleRepository {

    final static int PARALLELISM_THRESHOLD = 10000;
    final private ConcurrentHashMap<UUID, SaleDO> saleStorage;
    final private Map<UUID, SaleDO> saleStorage2;
    final private DateTimeHelper dateTimeHelper;

    private void initSaleStorage() {
        for (int i = 0; i < 2500000; i++) {
            SaleDO saleDO = new SaleDO(UUID.randomUUID(), 1000.0);
            saleStorage.put(saleDO.getId(), saleDO);
        }

    }

    private void initSaleStorage2() {
        for (int i = 0; i < 250000; i++) {
            SaleDO saleDO = new SaleDO(UUID.randomUUID(), 1000.0);
            saleStorage2.put(saleDO.getId(), saleDO);
        }

    }

    public SaleRepository() {
        saleStorage = new ConcurrentHashMap<>();
        dateTimeHelper = new DateTimeHelper();
        saleStorage2 = new HashMap<>();
        initSaleStorage2();
    }

    public SaleStatisticDTO calculateSalesStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        long startTime = System.currentTimeMillis();
        AtomicLong saleItemCount = new AtomicLong();
        final Map<UUID, UUID> markDeletableKeys = new ConcurrentHashMap<>();

        Double totalSaleWithinTimeInterval = saleStorage.reduce(PARALLELISM_THRESHOLD, (k, v) -> {
            double value = 0;
            if (dateTimeHelper.isWithinRange(v.getDate(), startDateTime, endDateTime)) {
                value = v.getSalesAmount();
                saleItemCount.getAndIncrement();
            }
            if (dateTimeHelper.isInDeleteRange(v.getDate(), endDateTime)) {
                markDeletableKeys.put(k, k);
            }

            return value;
        }, Double::sum);

        //free up memory
        CompletableFuture.runAsync(() -> deleteKeys(markDeletableKeys));

        final Double averageOrderAmount = saleItemCount.get() == 0 ? 0 : totalSaleWithinTimeInterval / saleItemCount.get();

        long endTime = System.currentTimeMillis();

        System.out.println("ConcurrentHashMap took " + (endTime - startTime) + " milliseconds");

        return new SaleStatisticDTO(String.valueOf(totalSaleWithinTimeInterval), String.valueOf(averageOrderAmount)); //
    }

    public SaleStatisticDTO calculateSalesStatistics2(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        long startTime = System.currentTimeMillis();
        AtomicLong saleItemCount = new AtomicLong();
        final Map<UUID, UUID> markDeletableKeys = new ConcurrentHashMap<>();

        Double totalSaleWithinTimeInterval = saleStorage2.entrySet().parallelStream().
        filter(entry -> {
            boolean value = false;
            if (dateTimeHelper.isWithinRange(entry.getValue().getDate(), startDateTime, endDateTime)) {
                    value = true;
            }
            if (dateTimeHelper.isInDeleteRange(entry.getValue().getDate(), endDateTime)) {
                markDeletableKeys.put(entry.getKey(), entry.getKey());
            }

            return value;
        }).map(entity -> entity.getValue().getSalesAmount()).reduce(Double::sum).orElse(0.0);

        //free up memory
        CompletableFuture.runAsync(() -> deleteKeys2(markDeletableKeys));

        final Double averageOrderAmount = saleItemCount.get() == 0 ? 0 : totalSaleWithinTimeInterval / saleItemCount.get();

        long endTime = System.currentTimeMillis();

        System.out.println("HashMap took " + (endTime - startTime) + " milliseconds");

        return new SaleStatisticDTO(String.valueOf(totalSaleWithinTimeInterval), String.valueOf(averageOrderAmount)); //
    }

    public SaleDO insert(SaleDO saleDO) {
        return saleStorage.put(saleDO.getId(), saleDO);
    }

    private void deleteKeys(final Map<UUID, UUID> keys) {
        for (var entry : keys.entrySet()) {
            saleStorage.remove(entry.getKey());
        }
        keys.clear();
    }

    private void deleteKeys2(final Map<UUID, UUID> keys) {
        for (var entry : keys.entrySet()) {
            saleStorage2.remove(entry.getKey());
        }
        keys.clear();
    }
}
