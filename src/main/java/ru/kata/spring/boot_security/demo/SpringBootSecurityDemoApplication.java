package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.DTOService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	@Autowired
	private DTOService<User, UserDTO> userDTOService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

	}
	@Component
	public class RunAfterInit {
		@EventListener(ApplicationReadyEvent.class)
		public void runAfterInit() {
			Role role = new Role(1L, "ROLE_USER");
			roleService.addRole(role);
			role = new Role(2L, "ROLE_ADMIN");
			roleService.addRole(role);
			UserDTO dto = new UserDTO(1L, "user", "100", "firstName", "lastName", 20,
					new ArrayList<>(List.of("USER")));
			User user = userDTOService.toEntity(dto);
			userService.addUser(user);

			dto = new UserDTO(2L, "admin", "100", "firstName2", "lastName2", 21,
					new ArrayList<>(List.of("ADMIN")));
			user = userDTOService.toEntity(dto);
			userService.addUser(user);
			dto = new UserDTO(3L, "admin2", "100", "firstName3", "lastName3", 21,
					new ArrayList<>(List.of("ADMIN")));
			user = userDTOService.toEntity(dto);
			userService.addUser(user);
			userService.updateUser(user);
		}
	}

}
