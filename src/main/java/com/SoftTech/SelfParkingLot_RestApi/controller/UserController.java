package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.entity.User;
import com.SoftTech.SelfParkingLot_RestApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getAll();
    }

    @GetMapping("/{Id}")
    public User getUser(@PathVariable Long id){
        return userService.get(id);
    }

    @PostMapping
    public User saveUser(@RequestBody User user){
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id){
        return userService.delete(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id){
        return userService.delete(id);
    }
}
