package ru.practicum.explore.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAllCategory(Integer from, Integer size) {
        log.info("Получение категорий  CategoryServiceImpl.findAllCategory");
        return categoryRepository
                .findAll(PageRequest.of(from / size, size))
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long catId) {
        log.info("Получение информации о категории по её идентификатору CategoryServiceImpl.getCategoryById" +
                "  catId = {}", catId);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s", catId)));
        return Optional.of(categoryMapper.toCategoryDto(category));
    }
}
