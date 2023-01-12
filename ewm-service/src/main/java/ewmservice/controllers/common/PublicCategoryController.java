package ewmservice.controllers.common;

import ewmservice.category.dto.CategoryDto;
import ewmservice.category.service.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicCategoryController {

    private final CategoryServiceImpl categoryService;

    public PublicCategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Get category={}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from",
            defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size",
                                                   defaultValue = "10") @Positive Integer size) {
        log.info("Get sorted categories from={}, sizer={}", from, size);
        return categoryService.getCategories(from, size);
    }
}
