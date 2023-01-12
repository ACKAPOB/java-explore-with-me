package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса категорий
 */
public interface CategoryService {
    /*
    Публичный метод контроллера для получения всех категорий
    */
    Collection<CategoryDto> findAllCategory(Integer from, Integer size);

    /*
    Публичный метод контроллера для получения категории по id
    */
    Optional<CategoryDto> getCategoryById(Long catId);
}
