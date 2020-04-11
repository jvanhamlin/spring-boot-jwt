package com.jonathonvanhamlin.springjwt.controller;

import com.jonathonvanhamlin.springjwt.dto.UserData;
import com.jonathonvanhamlin.springjwt.dto.UserResponse;
import com.jonathonvanhamlin.springjwt.model.User;
import com.jonathonvanhamlin.springjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public String login(@RequestParam String username, @RequestParam String password) {
        return userService.signin(username, password);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody UserData userData) {
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setRoles(userData.getRoles());
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());

        return userService.signup(user);
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponse search(@PathVariable String username) {
        User user = userService.search(username);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setRoles(user.getRoles());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")

    public UserResponse whoami(HttpServletRequest req) {
        User user = userService.whoami(req);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setRoles(user.getRoles());
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }
}
