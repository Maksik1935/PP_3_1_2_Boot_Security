package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService; //Можно ли импортировать сразу имплементацию?

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void getAttributes(Model model, Principal principal) {
        User user = userService.loadFullUserByUsername(principal.getName());
        model.addAttribute("username", userService.loadUserByUsername(principal.getName()).getUsername());
        model.addAttribute("roles", user.getRolesNames());; //Можно же проще? Без отдельного метода?
        model.addAttribute("user", userService.loadFullUserByUsername(principal.getName())); // Тот же вопрос
    }
    @GetMapping
    public ModelAndView getUserPage() {
        ModelAndView mav = new ModelAndView("user");
        return mav;
    }

}
