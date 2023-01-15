package ru.practicum.explore.category.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.category.service.AdminCategoryService;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        log.info("Редактирование события AdminCategoryServiceImpl.patchCategory categoryDto = {}", categoryDto);
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent())
            throw new ConflictException("Error name AdminCategoryServiceImpl.patchCategory");
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s", categoryDto.getId())));
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории AdminCategoryServiceImpl.postCategory newCategoryDto = {}", newCategoryDto);
        if (categoryRepository.findFirstByName(newCategoryDto.getName()).isPresent())
            throw new ConflictException("Error name AdminCategoryServiceImpl.patchCategory");
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        log.info("Удаление категории AdminCategoryServiceImpl.deleteCategory catId = {}", catId);
        categoryRepository
                .delete(categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s", catId))));
    }

}
