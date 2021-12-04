package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class SaleRepository {

    final static int PARALLELISM_THRESHOLD = 10000;
    final private ConcurrentHashMap<UUID, SaleDO> saleStorage = new ConcurrentHashMap<>();

    private void initSaleStorage() {
        for (int i = 0; i < 250000; i++) {
            SaleDO saleDO = new SaleDO(UUID.randomUUID(), ThreadLocalRandom.current().nextLong(0, 10000 + 1));
            saleStorage.put(saleDO.getId(), saleDO);
        }
    }

    public SaleRepository() {
        //initSaleStorage();
    }

    private boolean isWithinRange(LocalDateTime testDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        boolean ch1 = testDateTime.isBefore(startDateTime);
        boolean ch2 = testDateTime.isAfter(endDateTime);



        boolean x =  testDateTime.isBefore(startDateTime) && testDateTime.isAfter(endDateTime);

        return x;
    }

    public Long calculateSalesStatistics() {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.minusMinutes(1);

        System.out.println("Start datetime ----> " + startDateTime);
        System.out.println("End datetime ----> " + endDateTime);
        return saleStorage.reduce(PARALLELISM_THRESHOLD, (k, v) ->
                        isWithinRange(v.getDate(), startDateTime, endDateTime) ? v.getSalesAmount() : 0
                , Long::sum);
    }

    public SaleDO insert(SaleDO saleDO) {
        System.out.println(saleStorage.size());
        System.out.println(saleDO.getDate());
        return saleStorage.put(saleDO.getId(), saleDO);
    }

    public int size() {
        return saleStorage.size();
    }

}
