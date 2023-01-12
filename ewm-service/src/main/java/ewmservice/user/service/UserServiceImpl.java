package ewmservice.user.service;

import ewmservice.exception.ConflictException;
import ewmservice.exception.EntityNotFoundException;
import ewmservice.user.dto.UserDto;
import ewmservice.user.mapper.UserMapper;
import ewmservice.user.model.User;
import ewmservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ewmservice.user.mapper.UserMapper.toUser;
import static ewmservice.user.mapper.UserMapper.toUserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            return toUserDto(userRepository.save(toUser(userDto)));
        } catch (Exception e) {
            throw new ConflictException(String.format("%s - имя пользователя уже сущетсвует.", userDto.getName()));
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь %d не найден", userId)));
    }
}
