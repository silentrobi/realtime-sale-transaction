package com.example.mohammadabumusarabiul.dataaccessobject;

import com.example.mohammadabumusarabiul.datatransferobject.SaleStatisticDTO;
import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.example.mohammadabumusarabiul.util.DateTimeHelper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DefaultSaleRepository extends AbstractCrudRepository<SaleDO, UUID> implements SaleRepository {

    final static int PARALLELISM_THRESHOLD = 10000;
    final private ConcurrentHashMap<UUID, SaleDO> saleStorage;
    final private DateTimeHelper dateTimeHelper;

    private void initSaleStorage() {
        for (int i = 0; i < 250000; i++) {
            SaleDO saleDO = new SaleDO(UUID.randomUUID(), 1000.0);
            saleStorage.put(saleDO.getId(), saleDO);
        }
    }

    public DefaultSaleRepository(final ConcurrentHashMap<UUID, SaleDO> saleStorage, final DateTimeHelper dateTimeHelper) {
        super(saleStorage);
        this.saleStorage = saleStorage;
        this.dateTimeHelper = dateTimeHelper;
    }

    @Override
    public SaleStatisticDTO calculateSalesStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime) {

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

        return new SaleStatisticDTO(String.valueOf(totalSaleWithinTimeInterval), String.valueOf(averageOrderAmount)); //
    }

    private void deleteKeys(final Map<UUID, UUID> keys) {
        for (var entry : keys.entrySet()) {
            saleStorage.remove(entry.getKey());
        }
        keys.clear();
    }
}
