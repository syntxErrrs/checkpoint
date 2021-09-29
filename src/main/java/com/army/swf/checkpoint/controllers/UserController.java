package com.army.swf.checkpoint.controllers;

import com.army.swf.checkpoint.models.AuthDTO;
import com.army.swf.checkpoint.models.User;
import com.army.swf.checkpoint.models.UserDTO;
import com.army.swf.checkpoint.repositories.UserRepository;
import com.army.swf.checkpoint.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    Iterable<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    UserDTO saveNewUser(@RequestBody User user) {
        User savedUser = this.userRepository.save(user);
        return new UserDTO(savedUser);
    }

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable Long id) {
        //todo handle when user is not found
        User requestedUser = this.userRepository.findById(id).get();
        return new UserDTO(requestedUser);
    }

    @PatchMapping("/{id}")
    UserDTO updateUserEmail(@PathVariable Long id, @RequestBody HashMap<String, String> email) {
        //todo handle when user is not found
        User user = this.userRepository.findById(id).get();
        user.setEmail(email.get("email"));
        return new UserDTO(user);
    }

    @DeleteMapping("/{id}")
    Map<String, Long> deleteUserById(@PathVariable Long id) throws Exception {
        //todo handle when user is not found
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        Map<String, Long> returnMap = new HashMap<>();
        returnMap.put("count", this.userRepository.count());
        return returnMap;
    }

    @PostMapping("/authenticate")
    AuthDTO authenticateUser(@RequestBody User user) {
        UserDTO userDTO = this.userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return (userDTO != null) ? new AuthDTO(true, userDTO) : new AuthDTO(false);
    }

}
