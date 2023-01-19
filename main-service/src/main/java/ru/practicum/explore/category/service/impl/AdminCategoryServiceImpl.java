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
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto patch(CategoryDto categoryDto) {
        log.info("Редактирование события AdminCategoryServiceImpl.patch categoryDto = {}", categoryDto);
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent())
            throw new ConflictException("Error name AdminCategoryServiceImpl.patch");
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Категория не найдена patchCategory " +
                        "id = %s", categoryDto.getId())));
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto post(NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории AdminCategoryServiceImpl.post newCategoryDto = {}", newCategoryDto);
        if (categoryRepository.findFirstByName(newCategoryDto.getName()).isPresent())
            throw new ConflictException("Error name AdminCategoryServiceImpl.post");
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        log.info("Удаление категории AdminCategoryServiceImpl.delete catId = {}", catId);
        categoryRepository
                .delete(categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Категория не найдена " +
                        "delete id = %s", catId))));
    }

}
