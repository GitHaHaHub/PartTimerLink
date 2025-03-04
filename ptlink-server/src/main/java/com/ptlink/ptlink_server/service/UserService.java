package com.ptlink.ptlink_server.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.ptlink.ptlink_server.dto.UserInfo;
import com.ptlink.ptlink_server.dto.UserRequest;
import com.ptlink.ptlink_server.model.User;
import com.ptlink.ptlink_server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        else if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(null, userRequest.getUsername(), userRequest.getEmail());
        userRepository.save(user);
    }

    public UserInfo findUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        User foundUser = user.orElseThrow(() -> new RuntimeException("User not found"));
        return new UserInfo(foundUser.getUserId(), foundUser.getUsername(), foundUser.getEmail());
    }

    public void updateUser(UserRequest userRequest) {
        Optional<User> user = userRepository.findByUsername(userRequest.getUsername());
        User foundUser = user.orElseThrow(() -> new RuntimeException("User not found"));
        foundUser.setEmail(userRequest.getEmail());
        userRepository.save(foundUser);
    }

    public void deleteUser(String username) {   
        Optional<User> user = userRepository.findByUsername(username);
        User foundUser = user.orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(foundUser);
    }
}
