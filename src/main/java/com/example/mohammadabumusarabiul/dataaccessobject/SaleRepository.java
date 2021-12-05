package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SaleRepository {

    final static int PARALLELISM_THRESHOLD = 10000;
    final private ConcurrentHashMap<UUID, SaleDO> saleStorage = new ConcurrentHashMap<>();

    private void initSaleStorage() {
        for (int i = 0; i < 2500000; i++) {
            SaleDO saleDO = new SaleDO(UUID.randomUUID(), 1000L);
            saleStorage.put(saleDO.getId(), saleDO);
        }
    }

    public SaleRepository() {
        initSaleStorage();
    }

    private boolean isWithinRange(LocalDateTime testDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        boolean x = (testDateTime.isEqual(startDateTime) || testDateTime.isBefore(startDateTime)) &&
                (testDateTime.isEqual(endDateTime) || testDateTime.isAfter(endDateTime));

        return x;
    }

    private boolean isInDeleteRange(LocalDateTime testDateTime, LocalDateTime endDateTime) {
        boolean x = testDateTime.isEqual(endDateTime) || testDateTime.isBefore(endDateTime);
        return x;
    }

    public SaleStatisticDTO calculateSalesStatistics() {

        long startTime = System.currentTimeMillis();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        AtomicLong saleItemCount = new AtomicLong();
        //final List<UUID> markDeletableKeys = new ArrayList<>(saleStorage.keySet());;
        System.out.println("Start datetime ----> " + startDateTime);
        System.out.println("End datetime ----> " + endDateTime);
        Long x = 0L;
//        x = saleStorage.reduce(PARALLELISM_THRESHOLD, (k, v) -> {
//            long value = 0;
//            if (isWithinRange(v.getDate(), startDateTime, endDateTime)) {
//                value = v.getSalesAmount();
//                saleItemCount.getAndIncrement();
//
//            }
//            if (isInDeleteRange(v.getDate(), endDateTime)) {
//                markDeletableKeys.add(k);
//            }
//            return value;
//        }, Long::sum);
        System.out.println("Execute method with configured executor - "
                + Thread.currentThread().getName());
        CompletableFuture.runAsync(() ->  deleteKeys(new ArrayList<>(saleStorage.keySet())));
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        System.out.println(saleStorage.size());
        System.out.println("sale storage: " + saleStorage.size());
        return new SaleStatisticDTO(String.valueOf(x), String.valueOf(5));
    }

    public SaleDO insert(SaleDO saleDO) {
        System.out.println(saleStorage.size());
        System.out.println(saleDO.getDate());
        return saleStorage.put(saleDO.getId(), saleDO);
    }

    public void deleteKeys(List<UUID> keys) {
        System.out.println("Execute method with configured executor - "
                + Thread.currentThread().getName());
        for (UUID key : keys) {
            saleStorage.remove(key);
        }
    }

    public int size() {
        return saleStorage.size();
    }

}
