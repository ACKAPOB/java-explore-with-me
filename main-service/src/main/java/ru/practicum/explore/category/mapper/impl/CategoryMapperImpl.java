package ru.practicum.explore.category.mapper.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    @Override
    public void updateCategoryFromCategoryDto(CategoryDto categoryDto, Category category) {
        if (categoryDto.getId() != null)
            category.setId(category.getId());
        if (categoryDto.getName() != null)
            category.setName(categoryDto.getName());
    }
}
