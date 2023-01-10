package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(NewCategoryDto newCategoryDto);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    void deleteCategory(Long catId);
}
