package com.army.swf.checkpoint.services;

import com.army.swf.checkpoint.models.UserDTO;
import com.army.swf.checkpoint.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ArrayList<UserDTO> getAllUsers() {
        ArrayList<UserDTO> usersList = new ArrayList<>();
        this.userRepository.findAll().forEach(u -> usersList.add(new UserDTO(u)));
        return usersList;
    }
}
