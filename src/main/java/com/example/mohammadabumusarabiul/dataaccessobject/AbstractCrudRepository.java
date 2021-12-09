package com.example.mohammadabumusarabiul.dataaccessobject;

import java.util.Map;

public abstract class AbstractCrudRepository<T, Tkey> implements CrudRepository<T, Tkey> {

    private final Map<Tkey, T> storage;

    public AbstractCrudRepository(final Map<Tkey, T> storage) {
        this.storage = storage;
    }

    @Override
    public T findById(Tkey id) {
        return storage.get(id);
    }

    @Override
    public void upsert(Tkey id, T entity) {
        storage.put(id, entity);
    }

    @Override
    public void delete(Tkey id){
         storage.remove(id);
    }

    @Override
    public void deleteAll(){
        storage.clear();
    }
}
