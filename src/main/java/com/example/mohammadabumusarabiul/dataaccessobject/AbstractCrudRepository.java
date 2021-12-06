package com.example.mohammadabumusarabiul.dataaccessobject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCrudRepository<T, Tkey> implements CrudRepository<T, Tkey> {

    final private Map<Tkey, T> storage;

    public AbstractCrudRepository(final Map<Tkey, T> storage) {
        this.storage = storage;
    }

    @Override
    public T findById(Tkey id) {
        return storage.get(id);
    }

    @Override
    public T upsert(Tkey id, T entity) {
        return storage.put(id, entity);
    }

    @Override
    public void delete(Tkey id){
         storage.remove(id);
    }
}
