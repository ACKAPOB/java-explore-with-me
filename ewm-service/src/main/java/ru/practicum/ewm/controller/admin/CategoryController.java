package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping()
    public CategoryDto createCategory(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        //validateCategoryName(newCategoryDto);
        log.info("Создание категории CategoryController.createCategory Category = {}",
                newCategoryDto);
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto patchCategory(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Обновление категории CategoryController.patchCategory categoryDto = {}", newCategoryDto);
        return categoryService.patchCategory(newCategoryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        log.info("Удаление категории  CategoryController.deleteCategory id = {}", id);
        categoryService.deleteCategory(id);
    }
}
