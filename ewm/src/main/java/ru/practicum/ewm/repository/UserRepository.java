package main.java.ru.practicum.ewm.repository;

import main.java.ru.practicum.ewm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
