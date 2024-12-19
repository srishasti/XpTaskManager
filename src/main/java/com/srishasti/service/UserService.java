package com.srishasti.service;

import com.srishasti.context.UserContext;
import com.srishasti.controller.UserController;
import com.srishasti.model.User;
import com.srishasti.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void addUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }


    public String verifyUser(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
        if(authentication.isAuthenticated()){
            int userId = userRepo.findByUsername(user.getUsername()).getId();
            return jwtService.generateToken(user.getUsername(), userId);
        }
        return "";
    }

    public User getCurrentUser() {
        int userId = UserContext.getUserId();
        return userRepo.findById(userId).orElse(null);
    }
}
