package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User,Integer> {

}
