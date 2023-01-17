package ru.practicum.explore.location.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.location.repository.LocationRepository;
import ru.practicum.explore.location.service.LocationService;

@Service
@Slf4j
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
