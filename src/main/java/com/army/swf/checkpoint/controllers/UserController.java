package com.army.swf.checkpoint.controllers;

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
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = this.userRepository.findById(id);
        UserDTO userDTO = user.map(UserDTO::new).orElseGet(UserDTO::new);
        HttpStatus httpStatus = (user.isPresent()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(userDTO, httpStatus);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserDTO> updateUserEmail(@PathVariable Long id, @RequestBody HashMap<String, String> email) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) user.get().setEmail(email.get("email"));
        UserDTO userDTO = user.map(UserDTO::new).orElseGet(UserDTO::new);
        HttpStatus httpStatus = (user.isPresent()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(userDTO, httpStatus);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Long>> deleteUserById(@PathVariable Long id) throws Exception {
        Optional<User> user = this.userRepository.findById(id);
        HttpStatus httpStatus = user.isPresent() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        if (user.isPresent()) this.userRepository.deleteById(id);
        Map<String, Long> returnMap = new HashMap<>();
        returnMap.put("count", this.userRepository.count());
        return new ResponseEntity<>(returnMap, httpStatus);
    }

    @PostMapping("/authenticate")
    AuthDTO authenticateUser(@RequestBody User user) {
        UserDTO userDTO = this.userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return (userDTO != null) ? new AuthDTO(true, userDTO) : new AuthDTO(false);
    }

}
