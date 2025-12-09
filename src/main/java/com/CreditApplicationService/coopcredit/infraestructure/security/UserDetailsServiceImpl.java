package com.CreditApplicationService.coopcredit.infraestructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // Simulación de base de datos en memoria
    private final Map<String, UserRecord> users = new ConcurrentHashMap<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser(String username, String password, String role) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        users.put(username, new UserRecord(username, password, role));
    }

    public boolean validateCredentials(String username, String rawPassword) {
        UserRecord user = users.get(username);
        if (user == null) return false;
        return passwordEncoder.matches(rawPassword, user.password);
    }

    public String getUserRole(String username) {
        UserRecord user = users.get(username);
        if (user == null) return null;
        return user.role;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRecord user = users.get(username);
        if (user == null) throw new UsernameNotFoundException("Usuario no encontrado");
        return User.withUsername(user.username)
                .password(user.password)
                .roles(user.role.replace("ROLE_", ""))
                .build();
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private static class UserRecord {
        String username;
        String password;
        String role;
        UserRecord(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}
