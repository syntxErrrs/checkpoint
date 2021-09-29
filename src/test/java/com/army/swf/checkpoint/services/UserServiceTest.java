package com.army.swf.checkpoint.services;

import com.army.swf.checkpoint.models.User;
import com.army.swf.checkpoint.models.UserDTO;
import com.army.swf.checkpoint.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    User user = new User("test@email.com", "password");

    @BeforeEach
    void setup() {
        when(this.userRepository.findAll()).thenReturn(
                new ArrayList<User>() {{
                    add(user);
                }}
        );
    }

    @Test
    void getAllUsersReturnsListOfUserDTOs() {
        ArrayList<UserDTO> expected = new ArrayList<>() {{
            add(new UserDTO(user));
        }};
        ArrayList<UserDTO> actual = this.userService.getAllUsers();

        assertEquals(expected.get(0).getEmail(), actual.get(0).getEmail());
    }
}
