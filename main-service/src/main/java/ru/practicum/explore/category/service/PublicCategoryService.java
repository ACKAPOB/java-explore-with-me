package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface PublicCategoryService {

    List<CategoryDto> findAll(Integer from, Integer size);

    Optional<CategoryDto> get(Long catId);
}

