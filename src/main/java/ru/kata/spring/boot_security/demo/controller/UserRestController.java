package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.DTOService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final DTOService<User, UserDTO> userDTOService;

    public UserRestController(UserService userService, DTOService<User, UserDTO> userDTOService) {
        this.userService = userService;
        this.userDTOService = userDTOService;
    }

    @GetMapping("/getUserPage")
    public ResponseEntity<UserDTO> getUserPage(Principal principal) {
        return ResponseEntity.ok(userDTOService.toDTO(userService.loadUserByUsername(principal.getName())));
    }

}
