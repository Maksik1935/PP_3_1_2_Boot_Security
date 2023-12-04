package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.DTOService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DTOService<User, UserDTO> userDTOService;

    public UserController(UserService userService, DTOService<User, UserDTO> userDTOService) {
        this.userService = userService;
        this.userDTOService = userDTOService;
    }

    @ModelAttribute
    public void getAttributes(Model model, Principal principal) {
        UserDTO currentUser = userDTOService.toDTO(userService.loadUserByUsername(principal.getName()));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserRolls", currentUser.getRoleNames());
    }
    @GetMapping
    public String getUserPage() {
        return "user";
    }

}
