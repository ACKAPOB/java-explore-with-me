package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> findAllCategory(Integer from, Integer size);

    Optional<CategoryDto> getCategoryById(Long catId);
}
