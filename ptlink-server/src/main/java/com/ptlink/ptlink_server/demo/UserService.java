package com.ptlink.ptlink_server.demo;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.ptlink.ptlink_server.model.Role;
import com.ptlink.ptlink_server.model.User;
import com.ptlink.ptlink_server.repository.RoleRepository;
import com.ptlink.ptlink_server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        else if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = new Role(null, "USER");
        role = roleRepository.save(role);

        User user = new User(null, userRequest.getUsername(), userRequest.getEmail(), passwordEncoder.encode("pswrd"), role);
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
