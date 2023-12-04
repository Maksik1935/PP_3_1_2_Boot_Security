package ru.kata.spring.boot_security.demo.service.impl.entity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository dao) {
        this.userRepository = dao;
    }



    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }


    @Override
    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }


    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }



    @Override
    public void deleteUser(Long id) {
        userRepository.delete(findUser(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
