package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.service.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    public AdminCompilationController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("обавление новой подборки newCompilationDto = {} " +
                "AdminCompilationController. createCompilation",newCompilationDto);
        return adminCompilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удаление подборки id = {} AdminCompilationController.deleteCompilation", compId);
        adminCompilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Удалить событие из подборки event eventId = {}, compId = {} " +
                "AdminCompilationController.deleteEventInCompilation", eventId, compId);
        adminCompilationService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Добавить событие в подборку eventId = {} in compId id = {} " +
                "AdminCompilationController.addEventInCompilation", eventId, compId);
        adminCompilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Открепить подборку на главной странице compId = {} " +
                "AdminCompilationController.unpinCompilation", compId);
        adminCompilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Закрепить подборку на главной странице compId = {} " +
                "AdminCompilationController.pinCompilation", compId);
        adminCompilationService.pinCompilation(compId);
    }
}
