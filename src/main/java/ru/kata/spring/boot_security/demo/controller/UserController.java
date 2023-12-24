package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUser(ModelMap map, Principal principal) {
        Optional<User> currentUser = userService.getByUsername(principal.getName());
        if (currentUser.isEmpty()) {
            return "notfound";
        }
        map.addAttribute("currentUser", currentUser.orElseThrow(
                () -> new UsernameNotFoundException("Principal user not found")
        ));
        return "user";
    }
}
