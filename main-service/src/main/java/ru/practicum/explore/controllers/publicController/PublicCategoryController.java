package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Collection<CategoryDto> findAllCategory(@RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение категорий  PublicCategoryController.findAllCategory");
        return categoryService.findAllCategory(from, size);
    }

    @GetMapping("/{catId}")
    public Optional<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("Получение информации о категории по её идентификатору PublicCategoryController.getCategoryById" +
                "  catId = {}", catId);
        return categoryService.getCategoryById(catId);
    }
}


