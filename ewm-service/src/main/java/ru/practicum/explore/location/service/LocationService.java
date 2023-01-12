package ru.practicum.explore.location.service;

import ru.practicum.explore.location.model.Location;

/**
 * Интерфейс сервиса локации
 */
public interface LocationService {
    /*
    Метод сохранения локации
    */
    Location save(Location location);
}
