package com.runaumov.spring.dao;

import java.util.Optional;

public interface iDao<T, ID> {

    Optional<T> findById(ID id);

    T create(T model);

    Optional<T> update(T model);

    void delete(T model);
}
