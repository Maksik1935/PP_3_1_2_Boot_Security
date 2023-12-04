package ru.kata.spring.boot_security.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute
    public void getAttributes(Model model, Principal principal) {
        User user = userService.loadFullUserByUsername(principal.getName());
        model.addAttribute("username", userService.loadUserByUsername(principal.getName()).getUsername());
        model.addAttribute("roles", user.getRolesNames());
        model.addAttribute("fullRoles", roleService.getAllRoles());
        model.addAttribute("user", new User());
    }
    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView mav = new ModelAndView("admin");
        mav.addObject("users", userService.getAllUsers());
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView add(@ModelAttribute User userToSave) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        userService.addUser(userToSave);
        return mav;
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> find(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }
    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute User userToUpdate) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        userService.updateUser(userToUpdate);
        return mav;
    }

    @PostMapping("/delete")
    public ModelAndView delete(@ModelAttribute User userToDelete, @RequestParam Long id) {
        ModelAndView mav = new ModelAndView("redirect:/admin");
        userToDelete.setId(id);
        userService.deleteUser(userToDelete.getId());
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> studentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
}
