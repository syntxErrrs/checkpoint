package com.army.swf.checkpoint.controllers;

import com.army.swf.checkpoint.models.User;
import com.army.swf.checkpoint.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Rollback
    @Test
    void getAllUsers() throws Exception {
        String firstEmail = "email@email.com";
        String secondEmail = "anotherOne@example.com";
        User firstUser = new User(firstEmail, "this_is_a_pw");
        User secondUser = new User(secondEmail, "password");
        this.userRepository.save(firstUser);
        this.userRepository.save(secondUser);

        MockHttpServletRequestBuilder request = get("/users");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email", is(firstEmail)))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].email", is(secondEmail)))
                .andExpect(jsonPath("$[1].password").doesNotHaveJsonPath());
    }

    @Transactional
    @Rollback
    @Test
    void saveNewUser() throws Exception {
        String jsonRequest = """
                {
                    "email": "example@example.com",
                    "password": "b0s5m@n69"
                }
                """;

        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("example@example.com")));
    }

    @Transactional
    @Rollback
    @Test
    void getUserById() throws Exception {
        this.userRepository.save(new User("cool_guy@hotmail.com", "br1tt@ny_spearz"));
        this.userRepository.save(new User("email@email.com", "password"));
        ArrayList<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> users.add(user));
        User firstUser = users.get(0);
        Long firstUserId = firstUser.getId();
        String firstUserEmail = firstUser.getEmail();
        String urlTemplate = "/users/" + firstUserId;

        MockHttpServletRequestBuilder request = get(urlTemplate);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(firstUserEmail)));
    }

    @Transactional
    @Rollback
    @Test
    void updateUserEmailById() throws Exception {
        this.userRepository.save(new User("cool_guy@hotmail.com", "br1tt@ny_spearz"));
        this.userRepository.save(new User("email@email.com", "password"));
        ArrayList<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> users.add(user));
        User secondUser = users.get(1);
        Long secondUserId = secondUser.getId();
        String urlTemplate = "/users/" + secondUserId;
        String jsonRequest = """
                {
                    "email": "rockosmodernlife@example.com"
                }
                """;

        MockHttpServletRequestBuilder request = patch(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("rockosmodernlife@example.com")));
    }

    @Transactional
    @Rollback
    @Test
    void deleteUserById() throws Exception {
        this.userRepository.save(new User("cool_guy@hotmail.com", "br1tt@ny_spearz"));
        this.userRepository.save(new User("email@email.com", "password"));
        this.userRepository.save(new User("gamer@msn.com", "wontguessme"));
        ArrayList<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> users.add(user));
        User firstUser = users.get(0);
        Long firstUserId = firstUser.getId();
        String urlTemplate = "/users/" + firstUserId;

        MockHttpServletRequestBuilder request = delete(urlTemplate);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(2)));
    }

    @Transactional
    @Rollback
    @Test
    void authenticateUser() throws Exception {
        String email = "cool_guy@hotmail.com";
        String password = "br1tt@ny_spearz";
        this.userRepository.save(new User(email, password));
        String jsonRequest = """
                {
                    "email": "cool_guy@hotmail.com",
                    "password": "br1tt@ny_spearz"
                }
                """;
        String badJsonRequest = """
                {
                    "email": "cool_guy@hotmail.com",
                    "password": "incorrect"
                }
                """;

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true)))
                .andExpect(jsonPath("$.user.email", is(email)));

        request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJsonRequest);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(false)))
                .andExpect(jsonPath("$.user").doesNotHaveJsonPath());

    }


}
