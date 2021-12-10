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

    private static final int PARALLELISM_THRESHOLD = 1;
    private final ConcurrentHashMap<UUID, SaleDO> saleStorage;
    private final DateTimeHelper dateTimeHelper;

    public DefaultSaleRepository(final ConcurrentHashMap<UUID, SaleDO> saleStorage, final DateTimeHelper dateTimeHelper) {
        super(saleStorage);
        this.saleStorage = saleStorage;
        this.dateTimeHelper = dateTimeHelper;
    }

    /**
     * calculate sales statistics and free up memory asynchronously
     * by deleting sale objects of those has datetime before given endDateTime.
     *
     * @param startDateTime
     * @param endDateTime
     * @return return sale statistics
     */
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

        if (totalSaleWithinTimeInterval == null) {
            totalSaleWithinTimeInterval = 0.0;
        }

        //free up memory
        CompletableFuture.runAsync(() -> deleteKeys(markDeletableKeys));

        final Double averageOrderAmount = saleItemCount.get() == 0 ? 0.0 : totalSaleWithinTimeInterval / saleItemCount.get();

        return new SaleStatisticDTO(String.format("%.2f", totalSaleWithinTimeInterval), String.format("%.2f", averageOrderAmount));
    }

    private void deleteKeys(final Map<UUID, UUID> keys) {
        for (var entry : keys.entrySet()) {
            saleStorage.remove(entry.getKey());
        }
    }
}
