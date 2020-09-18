package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public void addUser(@Valid @RequestBody User user) {
       userService.addRegisterUser(user);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable int id){
        userService.deleteUser(id);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage = "invalid user";;
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

}
