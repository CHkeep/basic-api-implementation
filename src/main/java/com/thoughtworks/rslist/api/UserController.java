package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

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
//  @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
