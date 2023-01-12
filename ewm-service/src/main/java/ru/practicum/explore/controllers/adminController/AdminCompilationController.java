package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.event.service.EventService;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
public class AdminCompilationController {
    private final EventService eventService;

    @Autowired
    public AdminCompilationController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("обавление новой подборки newCompilationDto = {} " +
                "AdminCompilationController. createCompilation",newCompilationDto);
        return eventService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удаление подборки id = {} AdminCompilationController.deleteCompilation", compId);
        eventService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Удалить событие из подборки event eventId={}, compId={} " +
                "AdminCompilationController.deleteEventInCompilation", eventId, compId);
        eventService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Добавить событие в подборку eventId = {} in compId id = {} " +
                "AdminCompilationController.addEventInCompilation", eventId, compId);
        eventService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Открепить подборку на главной странице compId = {} " +
                "AdminCompilationController.unpinCompilation", compId);
        eventService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Закрепить подборку на главной странице compId = {} " +
                "AdminCompilationController.pinCompilation", compId);
        eventService.pinCompilation(compId);
    }
}
