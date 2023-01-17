package ru.practicum.explore.category.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.PublicCategoryService;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
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
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Категория не найдена " +
                        "getCategoryById id = %s", catId)));
        return Optional.of(categoryMapper.toCategoryDto(category));
    }

}
