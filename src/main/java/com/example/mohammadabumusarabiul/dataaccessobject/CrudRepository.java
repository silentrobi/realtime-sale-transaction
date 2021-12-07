package com.example.mohammadabumusarabiul.dataaccessobject;

public interface CrudRepository<T, Tkey> {

    T findById(Tkey id);
    void upsert(Tkey id, T entity);
    void delete(Tkey id);
}
