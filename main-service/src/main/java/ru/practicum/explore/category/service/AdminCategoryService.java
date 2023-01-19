package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto patch(CategoryDto categoryDto);

    CategoryDto post(NewCategoryDto newCategoryDto);

    void delete(Long catId);
}

