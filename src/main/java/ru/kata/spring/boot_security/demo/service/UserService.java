package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();

    User findById(long id);

    boolean saveUser(User user);

    User findByEmail(String email);

    void deleteUser(long id);
}
