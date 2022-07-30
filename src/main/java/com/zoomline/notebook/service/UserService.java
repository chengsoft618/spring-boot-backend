package com.zoomline.notebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoomline.notebook.exceptions.InfoExistsException;
import com.zoomline.notebook.model.User;
import com.zoomline.notebook.model.auth.request.LoginRequest;
import com.zoomline.notebook.model.auth.response.LoginResponse;
import com.zoomline.notebook.repository.UserRepository;
import com.zoomline.notebook.security.JWTUtils;

import java.util.logging.Logger;

@Service
public class UserService {

    private final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User userObject) {
        LOGGER.info("Called createUser() from UserService");
        if(!userRepository.existsByEmail(userObject.getEmail())) {
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        }else {
            throw new InfoExistsException("User with email: " + userObject.getEmail() + " already exists");
        }
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        LOGGER.info("Called loginUser() from UserService");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    public User findUserByEmailAddress(String email) {
        LOGGER.info("Called findUserByEmailAddress() from UserService");
        return userRepository.findUserByEmail(email);
    }
}
