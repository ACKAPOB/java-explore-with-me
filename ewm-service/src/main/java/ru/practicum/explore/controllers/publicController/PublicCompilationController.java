package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/compilations")
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public Collection<CompilationDto> getCompilationAll(@RequestParam Boolean pinned,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение подборок событий PublicCompilationController.getAll");
        return compilationService.getCompilationAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Optional<CompilationDto> getCompilation(@PathVariable Long compId) {
        log.info("Получение подборки событий по его id PublicCompilationController.getCompilation compId={}", compId);
        return compilationService.getCompilation(compId);
    }
}
