package ru.practicum.explore.category.mapper;


import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.model.Category;

/**
 * Интерфейс маппер категорий
 */
public interface CategoryMapper {
    /*
    метод маппера из котегории в dto категории
    */
    CategoryDto toCategoryDto(Category category);

    /*
    Метод маппера из dto новой категории в категорию
    */
    Category toCategory(NewCategoryDto newCategoryDto);

    /*
    Метод маппера по обновлению категорий
    */
    void updateCategoryFromCategoryDto(CategoryDto categoryDto, Category category);
}
