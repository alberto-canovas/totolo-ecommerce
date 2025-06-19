package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.repository.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    IUserDao userDao;

    public User addNewUser (User user){
        return userDao.save(user);
    }

    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    //uso de Optional para que no nos devuelva null si no existe el usuario con id

    public Optional<User> getUserById(Integer id){
        return userDao.findById(id);
    }

    public void deleteUserById(Integer id){
        userDao.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userDao.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userDao.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

}
