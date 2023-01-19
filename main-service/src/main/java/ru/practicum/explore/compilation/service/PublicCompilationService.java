package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;

import java.util.List;
import java.util.Optional;

public interface PublicCompilationService {

    List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);

    Optional<CompilationDto> get(Long compId);
}
