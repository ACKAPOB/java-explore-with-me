package ewmservice.category.service;

import ewmservice.category.dto.CategoryDto;
import ewmservice.category.dto.NewCategoryDto;
import ewmservice.category.mapper.CategoryMapper;
import ewmservice.category.repository.CategoryRepository;
import ewmservice.exception.ConflictException;
import ewmservice.exception.EntityNotFoundException;
import ewmservice.exception.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ewmservice.category.mapper.CategoryMapper.toCategory;
import static ewmservice.category.mapper.CategoryMapper.toCategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Поля категории должны быть уникальными.");
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto newCategoryDto) {
        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Поля категории должны быть уникальными.");
        }
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категория %d не найдена", catId))));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name").descending());
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long catId) {
        validateCategoryForDelete(catId);
        categoryRepository.deleteById(catId);
    }

    private void validateCategoryForDelete(Long catId) {
        if (categoryRepository.findEventLinkedWithCategory(catId) != null) {
            throw new ValidationException("Нельзя удалить категорию, с которой связано событие.");
        }
    }
}
