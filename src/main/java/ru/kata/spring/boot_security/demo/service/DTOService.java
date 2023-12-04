package ru.kata.spring.boot_security.demo.service;

import java.util.List;

public interface DTOService <E, D> {
    D toDTO (E entity);

    E toEntity (D dto);

    List<D> listToDTO (List<E> entityList);
}
