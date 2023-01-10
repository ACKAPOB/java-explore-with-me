package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.mapper.CategoryMapper.toCategory;
import static ru.practicum.ewm.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Запрос на добавление Category, CategoryServiceImpl.createCategory category = {}", newCategoryDto);
        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Поля категории должны быть уникальными CategoryServiceImpl.createCategory");
        }
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(NewCategoryDto newCategoryDto) {
        log.info("Запрос на обновление Category, CategoryServiceImpl.patchCategory" + newCategoryDto);
        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Поля категории должны быть уникальными CategoryServiceImpl.patchCategory");
        }
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение информации о категории по её идентификатору, " +
                "CategoryServiceImpl.getCategoryById id = {}", catId);
        return toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория %d не найдена " +
                        "CategoryServiceImpl.patchCategory", catId))));
    }

    @Override
    @Transactional
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Запрос списка Category from = {}, size = {}, CategoryServiceImpl.getCategories", from, size);
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Запрос на удаление category с id = {}, CategoryServiceImpl.deleteCategory", catId);
        if (categoryRepository.findEventLinkedWithCategory(catId) != null)
                throw new ValidationException("Нельзя удалить категорию, с которой связано событие " +
                        "CategoryServiceImpl.deleteCategory");
        categoryRepository.deleteById(catId);
    }
}
