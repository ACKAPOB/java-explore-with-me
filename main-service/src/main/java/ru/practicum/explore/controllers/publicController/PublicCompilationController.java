package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.impl.PublicCompilationServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compilations")
@Slf4j
public class PublicCompilationController {
    private final PublicCompilationServiceImpl publicCompilationService;

    public PublicCompilationController(PublicCompilationServiceImpl publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilationAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение подборок событий PublicCompilationController.getAll");
        return publicCompilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Optional<CompilationDto> getCompilation(@PathVariable Long compId) {
        log.info("Получение подборки событий по его id PublicCompilationController.getCompilation compId={}", compId);
        return publicCompilationService.get(compId);
    }
}
