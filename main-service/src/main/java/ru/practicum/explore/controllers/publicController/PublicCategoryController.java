package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.PublicCategoryService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {
    private final PublicCategoryService categoryService;

    @Autowired
    public PublicCategoryController(PublicCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> findAllCategory(@RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение категорий  PublicCategoryController.findAllCategory");
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public Optional<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("Получение информации о категории по её идентификатору PublicCategoryController.getCategoryById" +
                "  catId = {}", catId);
        return categoryService.get(catId);
    }
}


