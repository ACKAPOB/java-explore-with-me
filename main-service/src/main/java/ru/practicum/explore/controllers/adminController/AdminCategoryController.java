package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.event.service.EventService;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
public class AdminCategoryController {
    private final EventService eventService;

    @Autowired
    public AdminCategoryController(EventService eventService) {
        this.eventService = eventService;
    }

    @PatchMapping
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Редактирование события AdminCategoryController.patchCategory categoryDto = {}", categoryDto);
        return eventService.patchCategory(categoryDto);
    }

    @PostMapping
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("AdminCategoryController.postCategory newCategoryDto = {}", newCategoryDto);
        return eventService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удаление категории AdminCategoryController.deleteCategory catId = {}", catId);
        eventService.deleteCategory(catId);
    }

}
