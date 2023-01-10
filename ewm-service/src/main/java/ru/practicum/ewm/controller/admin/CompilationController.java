package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.service.impl.CompilationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationServiceImpl compilationService;

    @PostMapping
    public CompilationDto createCompilation(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Создание подборки событий CompilationsController.createCompilation newCompilationDto = {}",
                newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public void deleteCompilation(@PathVariable Long id, @PathVariable Long eventId) {
        log.info("Удаление события из подборки CompilationsServiceImpl.deleteCompilation id = {}" +
                ", eventId = {}", id, eventId);
        compilationService.deleteEventFromCompilation(id, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public NewCompilationDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Добавление события из подборки CompilationsServiceImpl.addEventToCompilation compId = {}, " +
                "eventId = {}", compId, eventId);
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deletePinCompilation(@PathVariable Long compId) {
        log.info("Открепить подборку на главной странице CompilationsServiceImpl.deletePinCompilation id = {}", compId);
        compilationService.deletePinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Прикрепить подборку на главной странице CompilationsServiceImpl.pinCompilation id = {}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{id}")
    public void deleteCompilation(@PathVariable Long id) {
        log.info("Удаление подборки событий CompilationsController.deleteCompilation id = {}", id);
        compilationService.deleteCompilation(id);
    }
}
