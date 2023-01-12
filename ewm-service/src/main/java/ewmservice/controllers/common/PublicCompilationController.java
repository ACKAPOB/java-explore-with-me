package ewmservice.controllers.common;

import ewmservice.compilation.dto.CompilationDto;
import ewmservice.compilation.service.CompilationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicCompilationController {
    private final CompilationServiceImpl compilationService;

    public PublicCompilationController(CompilationServiceImpl compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Get compilation={}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getRequiredCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                        @RequestParam(name = "from",
                                                                defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(name = "size",
                                                                defaultValue = "10") @Positive Integer size) {
        return compilationService.getRequiredCompilations(pinned, from, size);
    }
}
