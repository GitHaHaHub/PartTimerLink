package com.ptlink.ptlink_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ptlink.ptlink_server.dto.UserRequest;
import com.ptlink.ptlink_server.service.UserService;

@RestController
public class DBController {
    @Autowired
    private UserService userService;

    @PostMapping("/AddUser")
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        try {
            userService.addUser(userRequest);    
            return ResponseEntity.ok("User added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add user");
        }
        
    }

    @GetMapping("/FindUser/{username}")
    public ResponseEntity<String> findUser(@PathVariable("username") String username) {
        try {
            userService.findUser(username);
            return ResponseEntity.ok("User found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PutMapping("/UpdateUser")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest) {
        try {
            userService.updateUser(userRequest);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user");
        }
    }

    @DeleteMapping("/DeleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete user");
        }
    }
}
