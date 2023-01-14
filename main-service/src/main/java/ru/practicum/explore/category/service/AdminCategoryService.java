package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;


public interface AdminCategoryService {

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);
}
