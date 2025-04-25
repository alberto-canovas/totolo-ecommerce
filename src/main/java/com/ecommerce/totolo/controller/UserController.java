package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/totolo/v1")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    @CrossOrigin
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    @CrossOrigin
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Integer id){
        Optional<User> optionalUser = userService.getUserById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            userService.deleteUserById(id);
            String message = "The user: "+user.getName()+ " "+user.getLastname()+" has been deleted successfully.";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("The user is not found",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    @CrossOrigin
    public ResponseEntity<String> addNewUser(@RequestBody User user){
        try{
            userService.addNewUser(user);
            String message = "The user: "+user.getName()+" has been added successfully.";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }catch(Exception ex){
            return  new ResponseEntity<>("Error the user can't be added: "+ ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/{id}")
    @CrossOrigin
    public ResponseEntity<String> updateUserById(@PathVariable("id") Integer id, @RequestBody User user ){
        Optional<User> optionalUser = userService.getUserById(id);
        if(optionalUser.isPresent()){
            User existentUser = optionalUser.get();
            existentUser.setName(user.getName());
            existentUser.setLastname(user.getLastname());
            existentUser.setAddress(user.getAddress());
            existentUser.setEmail(user.getEmail());
            existentUser.setPhone_number(user.getPhone_number());
            existentUser.setUsername(user.getUsername());
            userService.addNewUser(existentUser);
            return new ResponseEntity<>("The user is updated successfully",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("The user is not found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}")
    @CrossOrigin
    public ResponseEntity<User> getUserById(@PathVariable("id")Integer id){
        Optional<User> optionalUser = userService.getUserById(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }






}
