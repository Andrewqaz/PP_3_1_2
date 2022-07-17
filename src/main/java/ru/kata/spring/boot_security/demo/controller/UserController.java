package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping(value = "/")
public class UserController {
    private final UserService service;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("admin")
    public String getAdminPage(Model model) {
        List<User> allUsers = service.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("user")
    public String getUserPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", service.getUserByLogin(user.getLogin()));
        return "user";
    }

    @GetMapping("admin/adduser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", service.listRoles());
        return "adduser";
    }

    @PostMapping("admin/adduser")
    public String addUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        service.deleteUserById(userId);

        return "redirect:/admin";
    }

    @GetMapping("admin/update/{id}")
    public String updateUserForm(@PathVariable("id") int id, Model model) {
        User user = service.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", service.listRoles());
        return "update";
    }

    @PatchMapping("/admin/update/{id}")
    public String updateUser(User user, @PathVariable("id") int id) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.updateUser(user);
        return "redirect:/admin";
    }
}
