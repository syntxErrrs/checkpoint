package com.army.swf.checkpoint.repositories;

import com.army.swf.checkpoint.models.User;
import com.army.swf.checkpoint.models.UserDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<User, Long> {
    ArrayList<User> findAll();
    UserDTO findByEmailAndPassword(String email, String password);
}
