package ewmservice.controllers.admin;

import ewmservice.compilation.dto.CompilationDto;
import ewmservice.compilation.dto.NewCompilationDto;
import ewmservice.compilation.service.CompilationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static ewmservice.utilities.Validator.validateNewCompilationDto;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class CompilationController {

    private final CompilationServiceImpl compilationService;

    public CompilationController(CompilationServiceImpl compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping("/compilations")
    public CompilationDto saveCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Creating compilation={}", newCompilationDto);
        validateNewCompilationDto(newCompilationDto);
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Delete event={} from compilation={}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public NewCompilationDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Add event={} to compilation={}", eventId, compId);
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unPinCompilation(@PathVariable Long compId) {
        log.info("Unpin compilation={}", compId);
        compilationService.unPinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Pin compilation={}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
