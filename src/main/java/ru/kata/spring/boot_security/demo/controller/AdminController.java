package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    private AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public String listUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "list_users";
    }

    @GetMapping("/admin/create_user")
    public String createPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", new ArrayList<Role>());
        return "create_user";
    }

    @PostMapping("/admin/create_user")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "role") String[] roles) {
        user.setRoles(getRoles(roles));
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/admin/{id}/edit_user")
    public String editPage(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findById(id));
        return "edit_user";
    }

    @PatchMapping("/admin/{id}")
    public String updateUser(User user, @RequestParam(value = "role") String[] roles) {
        user.setRoles(getRoles(roles));
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    private Set<Role> getRoles(String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findByName(role));
        }
        return roleSet;
    }

    @GetMapping("/admin/{id}/remove_user")
    public String deletePage(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findById(id));
        return "/remove_user";
    }

    @DeleteMapping("/admin/user_delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
