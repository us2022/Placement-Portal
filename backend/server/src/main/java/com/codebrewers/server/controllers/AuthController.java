package com.codebrewers.server.controllers;

import com.codebrewers.server.exceptions.ResourceConflictException;
import com.codebrewers.server.models.User;
import com.codebrewers.server.payload.auth.UserLoginDto;
import com.codebrewers.server.payload.auth.UserRegistrationDto;
import com.codebrewers.server.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    // return created user
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            userService.save(userRegistrationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResourceConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User loggedInUser = this.userService.getUserByEmail(userLoginDto.getEmail());
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
