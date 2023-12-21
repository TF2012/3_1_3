package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.stream.Collectors;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    public final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException("User with this name nit found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList()));
    }
}
