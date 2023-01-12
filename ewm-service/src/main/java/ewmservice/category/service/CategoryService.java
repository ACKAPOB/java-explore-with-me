package ewmservice.category.service;

import ewmservice.category.dto.CategoryDto;
import ewmservice.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(NewCategoryDto newCategoryDto);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    void deleteCategory(Long catId);
}
