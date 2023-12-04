package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String firstName;

    private String lastName;

    private int age;
    private List<String> roleNames = new ArrayList<>();
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, String firstName, String lastName, int age, List<String> roleNames) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.roleNames = roleNames;
    }
}
