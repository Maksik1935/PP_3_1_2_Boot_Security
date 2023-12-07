package ru.kata.spring.boot_security.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.DTOService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.impl.entity.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final DTOService<User, UserDTO> userDTOService;
    private final RoleService roleService;

    public AdminRestController(UserServiceImpl userService, DTOService<User, UserDTO> userDTOService, RoleService roleService) {
        this.userService = userService;
        this.userDTOService = userDTOService;
        this.roleService = roleService;
    }

    //Как прописать ДТО сервис, чтобы оба не вызывать в контроллере? Не переопределять же там все методы userService...

    @GetMapping("/get-auth")
    public ResponseEntity<UserDTO> getAuthorisationUser() {
        return ResponseEntity.ok(userDTOService.toDTO(userService.loadUserByUsername("admin")));
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userDTOService.listToDTO(userService.getAllUsers()));
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<UserDTO> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(userDTOService.toDTO(userService.findUser(id)));
    }

    @GetMapping("roles-list")
    public ResponseEntity<List<String>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRolesNamesList());
    }

    //Обратный маппинг пропустил, не в этом суть задачи.
    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.addUser(userDTOService.toEntity(userDTO)));
    }
    //Выбрасывает stackOverflowError периодически (не всегда). Почему? Именно из фронта когда приходит сущность
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDTOService.toEntity(userDTO)));
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserNotFoundException.class) //Не нужен
    public ResponseEntity<?> studentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
}
