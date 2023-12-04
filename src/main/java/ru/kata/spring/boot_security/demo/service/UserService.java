package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User addUser(User user);
    User findUser(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();
    @Override
    User loadUserByUsername(String username);
}
