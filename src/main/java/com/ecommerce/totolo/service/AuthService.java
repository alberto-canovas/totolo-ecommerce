package com.ecommerce.totolo.service;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.ecommerce.totolo.dto.UserLoginDto;
import com.ecommerce.totolo.dto.UserRegisterDto;
import com.ecommerce.totolo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public User register(UserRegisterDto dto) {
        if (userService.usernameExists(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        if (userService.emailExists(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone_number(dto.getPhone_number());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword()); // 🔐 pendiente de hashear
        user.setType(TypeEnum.CLIENT);
        return userService.addNewUser(user);
    }

    public User login(UserLoginDto dto) {
        Optional<User> userOpt = userService.findByUsername(dto.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        return user;
    }
}
