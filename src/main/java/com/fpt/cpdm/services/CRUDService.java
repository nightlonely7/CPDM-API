package com.fpt.cpdm.services;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T, ID> {

    T save(T entity);

    List<T> saveAll(List<T> entities);

    T findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(List<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(List<T> entities);

    void deleteAll();
}
