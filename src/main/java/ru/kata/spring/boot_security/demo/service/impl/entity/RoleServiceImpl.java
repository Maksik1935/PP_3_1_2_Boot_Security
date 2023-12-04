package ru.kata.spring.boot_security.demo.service.impl.entity;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    public RoleServiceImpl(RoleRepository repo) {
        this.repo = repo;
    }

    public List<Role> getAllRoles() {
        return repo.findAll();
    }


    @Override
    public Role getByClearName(String name) {
        name = "ROLE_" + name;
        return repo.findByName(name);
    }


    @Override
    public Role addRole(Role role) {
        return repo.save(role);
    }


    @Override
    public List<String> getAllRolesNamesList() {
        return getAllRoles().stream()
                .map(Role::getName)
                .map(x -> x.replaceAll("ROLE_", ""))
                .collect(Collectors.toList());
    }
}
