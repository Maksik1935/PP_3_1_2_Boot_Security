package ru.kata.spring.boot_security.demo.service.impl.dto;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.DTOService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoServiceImpl implements DTOService<User, UserDTO> {
    private final RoleService roleService;

    public UserDtoServiceImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public UserDTO toDTO(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .map(x -> x.replaceAll("ROLE_", ""))
                .collect(Collectors.toList());
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                roleNames);
    }

    @Override
    public User toEntity(UserDTO dto) {
        List<Role> roles = dto.getRoleNames().stream()
                .map(roleService::getByClearName)
                .collect(Collectors.toList());
        return new User(dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAge()
                ,roles);
    }

    @Override
    public List<UserDTO> listToDTO(List<User> entityList) {
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
