package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.ecommerce.totolo.dto.UserDto;
import com.ecommerce.totolo.dto.UserLoginDto;
import com.ecommerce.totolo.dto.UserRegisterDto;
import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/totolo/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Integer id) {
        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            try {
                userService.deleteUserById(id);
                String message = "The user: " + optionalUser.get().getName() + " " + optionalUser.get().getLastname() + " has been deleted successfully.";
                return new ResponseEntity<>(message, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("The user is not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User user) {
        try {
            user.setType(TypeEnum.CLIENT);
            User savedUser = userService.addNewUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error the user can't be added: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Integer id,
                                                 @RequestBody User user,
                                                 @RequestParam Integer requesterId) {
        Optional<User> optionalTargetUser = userService.getUserById(id);
        Optional<User> optionalRequester = userService.getUserById(requesterId);

        if (optionalTargetUser.isEmpty() || optionalRequester.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        User targetUser = optionalTargetUser.get();
        User requester = optionalRequester.get();

        if (!requester.getType().equals(TypeEnum.ADMIN) && !requester.getId().equals(targetUser.getId())) {
            return new ResponseEntity<>("No tienes permisos para modificar a otros usuarios", HttpStatus.FORBIDDEN);
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            if (requester.getId().equals(targetUser.getId())) {
                targetUser.setPassword(user.getPassword());
            } else {
                return new ResponseEntity<>("Solo puedes modificar tu propia contraseña", HttpStatus.FORBIDDEN);
            }
        }

        if (user.getUsername() != null && !user.getUsername().equals(targetUser.getUsername())) {
            Optional<User> userWithSameUsername = userService.findByUsername(user.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(targetUser.getId())) {
                return new ResponseEntity<>("El nombre de usuario ya está en uso", HttpStatus.CONFLICT);
            }
            targetUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null && !user.getEmail().equals(targetUser.getEmail())) {
            Optional<User> userWithSameEmail = userService.findByEmail(user.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(targetUser.getId())) {
                return new ResponseEntity<>("El email ya está en uso", HttpStatus.CONFLICT);
            }
            targetUser.setEmail(user.getEmail());
        }

        if (user.getType() != null) {
            if (requester.getType().equals(TypeEnum.ADMIN)) {
                targetUser.setType(user.getType());
            } else {
                return new ResponseEntity<>("Solo un ADMIN puede cambiar el tipo de usuario", HttpStatus.FORBIDDEN);
            }
        }

        if (user.getName() != null) targetUser.setName(user.getName());
        if (user.getLastname() != null) targetUser.setLastname(user.getLastname());
        if (user.getAddress() != null) targetUser.setAddress(user.getAddress());
        if (user.getPhone_number() != null) targetUser.setPhone_number(user.getPhone_number());

        userService.addNewUser(targetUser);
        return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> optionalUser = userService.getUserById(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userDto) {
        if (userService.usernameExists(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso.");
        }

        if (userService.emailExists(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está en uso.");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhone_number(userDto.getPhone_number());
        user.setPassword(userDto.getPassword());
        user.setType(TypeEnum.CLIENT);

        userService.addNewUser(user);
        return ResponseEntity.ok("Usuario registrado correctamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto loginDto) {
        Optional<User> userOpt = userService.findByUsername(loginDto.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado.");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(loginDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta.");
        }

        return ResponseEntity.ok(new UserDto(user)); // ✅ DEVUELVE LOS DATOS DEL USUARIO
    }
}
