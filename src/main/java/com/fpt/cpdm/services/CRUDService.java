package com.fpt.cpdm.services;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> {

    T save(T model);

    List<T> saveAll(List<T> models);

    T findById(Integer id);

    boolean existsById(Integer id);

    List<T> findAll();

    List<T> findAllById(List<Integer> ids);

    long count();

    void deleteById(Integer id);

    void delete(T model);

    void deleteAll(List<T> models);

    void deleteAll();
}
