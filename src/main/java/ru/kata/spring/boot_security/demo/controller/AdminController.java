package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final DTOService<User, UserDTO> userDTOService;
    private final RoleService roleService;

    public AdminController(UserServiceImpl userService, DTOService<User, UserDTO> userDTOService, RoleService roleService) {
        this.userService = userService;
        this.userDTOService = userDTOService;
        this.roleService = roleService;
    }

    @ModelAttribute
    public void getAttributes(Model model, Principal principal) {
        UserDTO currentUser = userDTOService.toDTO(userService.loadUserByUsername(principal.getName()));
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("allUsers", userDTOService.listToDTO(userService.getAllUsers()));
        model.addAttribute("allRoles", roleService.getAllRolesNamesList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserRolls", currentUser.getRoleNames());
    }
    @GetMapping
    public String getAllUsers() {
        return "admin";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute UserDTO userDTO, @RequestParam(name = "roleToNew") String role) {
        userDTO.getRoleNames().add(role);
        userService.addUser(userDTOService.toEntity(userDTO));
        return "redirect:/admin";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute UserDTO userDTO, @RequestParam(name = "roleEdit") String role) {
        userDTO.getRoleNames().add(role);
        userService.updateUser(userDTOService.toEntity(userDTO));
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute User userDTO) {
        userService.deleteUser(userDTO.getId());
        return "redirect:/admin";
    }

    @ExceptionHandler(UserNotFoundException.class) //Не нужен
    public ResponseEntity<?> studentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
}
