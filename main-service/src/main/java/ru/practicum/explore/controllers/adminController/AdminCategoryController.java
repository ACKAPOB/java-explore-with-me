package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.service.impl.AdminCategoryServiceImpl;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
public class AdminCategoryController {
    private final AdminCategoryServiceImpl adminCategoryService;

    public AdminCategoryController(AdminCategoryServiceImpl adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PatchMapping
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Редактирование события AdminCategoryController.patchCategory categoryDto = {}", categoryDto);
        return adminCategoryService.patchCategory(categoryDto);
    }

    @PostMapping
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории AdminCategoryController.postCategory newCategoryDto = {}", newCategoryDto);
        return adminCategoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удаление категории AdminCategoryController.deleteCategory catId = {}", catId);
        adminCategoryService.deleteCategory(catId);
    }

}
