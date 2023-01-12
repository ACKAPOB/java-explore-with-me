package ewmservice.controllers.admin;

import ewmservice.category.dto.CategoryDto;
import ewmservice.category.dto.NewCategoryDto;
import ewmservice.category.service.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static ewmservice.utilities.Validator.validateCategoryName;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public CategoryDto saveCategory(@RequestBody NewCategoryDto newCategoryDto) {
        validateCategoryName(newCategoryDto);
        log.info("Creating category={}", newCategoryDto);
        return categoryService.saveCategory(newCategoryDto);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Creating category={}", newCategoryDto);
        validateCategoryName(newCategoryDto);
        return categoryService.updateCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Delete category={}", catId);
        categoryService.deleteCategory(catId);
    }
}
