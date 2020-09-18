package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.codec.ServerSentEvent.builder;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public void addRegisterUser(User user) {
        boolean isPresent = findByUserName(user.getUserName());
        if(!isPresent){
            addRegister(user);
        }
    }

    public void addRegister(User user){
        UserPO userPO = new UserPO();
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setGender(user.getGender());
        userPO.setUserName(user.getUserName());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
    }

    public boolean findByUserName(String name){
        boolean isPresent = userRepository.findAll().stream()
                .filter(userPO -> userPO.getUserName().equals(name))
                .findAny()
                .isPresent();

        return isPresent;
    }


    public User findById(int id) {
       Optional<UserPO> userPO = userRepository.findById(id);
       User user = User.builder().userName(userPO.get().getUserName())
               .age(userPO.get().getAge()).email(userPO.get().getEmail()).gender(userPO.get().getGender())
               .phone(userPO.get().getPhone()).voteNum(userPO.get().getVoteNum()).build();
       return user;
    }
}
