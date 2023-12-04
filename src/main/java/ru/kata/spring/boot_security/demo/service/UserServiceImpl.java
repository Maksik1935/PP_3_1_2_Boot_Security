package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
        private final UserRepository dao;

        public UserServiceImpl(UserRepository dao) {
            this.dao = dao;
        }

        @Override
        public User addUser(User user) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
            return dao.save(user);
        }

        @Override
        public User findUser(Long id) {
            return dao.findById(id).orElseThrow(UserNotFoundException::new);
        }

        @Override
        public User updateUser(User user) {
            return dao.save(user);
        }

        @Override
        public void deleteUser(Long id) {
            dao.delete(findUser(id));
        }

        @Override
        public List<User> getAllUsers() {
            return dao.findAll();
        }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = dao.findUserByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRoles());

    }

    public User loadFullUserByUsername(String username) {
        User user = dao.findUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("UserNotFound");
        }
        return user;
    }
}
