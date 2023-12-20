package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> getByIdForUpdate(Long id) {
        Optional<User> user = getById(id);
        if (user.isEmpty()) {
            return user;
        }
        user.get().setPassword("");
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User user) {
        Optional<User> uUser = userRepository.findById(id);
        if (uUser.isPresent()) {
            uUser.get().setUsername(user.getUsername());
            uUser.get().setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            uUser.get().setEmail(user.getEmail());
            uUser.get().setAge(user.getAge());
            uUser.get().setRoles(user.getRoles());
            userRepository.save(uUser.get());
        }
    }
}
