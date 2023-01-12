package ewmservice.utilities;

import ewmservice.category.dto.NewCategoryDto;
import ewmservice.compilation.dto.NewCompilationDto;
import ewmservice.event.dto.NewEventDto;
import ewmservice.exception.ValidationException;
import ewmservice.user.dto.UserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Validator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void validateUserName(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException(String.format("%s - некорректное имя пользователя.", userDto.getName()));
        }
    }

    public static void validateCategoryName(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("имя категории не может быть пустым.");
        }
    }

    public static void validateNewCompilationDto(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new ValidationException("Заголовок подборки не может быть пустым.");
        }
    }

    public static void validateEventId(Long eventId) {
        if (eventId == null) {
            throw new ValidationException("Поле ID события не может быть пустым");
        }
    }

    public static void validateEvent(NewEventDto newEventDto) {
        if (newEventDto.getTitle() == null || newEventDto.getTitle().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        if (newEventDto.getAnnotation() == null || newEventDto.getAnnotation().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        if (LocalDateTime.parse(newEventDto.getEventDate(), formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Событие должно начаться не раньшем, чем через два часа от текущего времени");
        }
    }
}
