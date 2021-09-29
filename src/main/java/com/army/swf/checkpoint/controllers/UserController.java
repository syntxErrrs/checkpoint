package com.army.swf.checkpoint.controllers;

import com.army.swf.checkpoint.exceptions.InvalidIdException;
import com.army.swf.checkpoint.models.AuthDTO;
import com.army.swf.checkpoint.models.User;
import com.army.swf.checkpoint.models.UserDTO;
import com.army.swf.checkpoint.repositories.UserRepository;
import com.army.swf.checkpoint.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return new UserDTO(this.userRepository.save(user));
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id) throws InvalidIdException {
        if (this.userRepository.findById(id).isEmpty()) throw new InvalidIdException(
                "Requested User ID does not exist."
        );
        return new ResponseEntity<>(new UserDTO(userRepository.findById(id).get()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserDTO> updateUserEmail(@PathVariable Long id, @RequestBody HashMap<String, String> email) throws InvalidIdException {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) throw new InvalidIdException("Requested User ID does not exist.");
        user.get().setEmail(email.get("email"));
        return new ResponseEntity<>(new UserDTO(user.get()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Long>> deleteUserById(@PathVariable Long id) throws InvalidIdException {
        if (this.userRepository.findById(id).isEmpty())
            throw new InvalidIdException("Requested User ID does not exist.");
        this.userRepository.deleteById(id);
        return new ResponseEntity<>(new HashMap<>() {{put("count", userRepository.count());}}, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    AuthDTO authenticateUser(@RequestBody User user) {
        UserDTO userDTO = this.userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return (userDTO != null) ? new AuthDTO(true, userDTO) : new AuthDTO(false);
    }

}
