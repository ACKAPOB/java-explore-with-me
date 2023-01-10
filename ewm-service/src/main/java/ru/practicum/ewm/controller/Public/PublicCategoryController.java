package ru.practicum.ewm.controller.Public;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Получение информации о категории по её идентификатору, " +
                "PublicCategoryController.getCategoryById id {}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping()
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос списка Category from = {}, size = {}, PublicCategoryController.getCategories", from, size);
        return categoryService.getCategories(from, size);
    }
}
