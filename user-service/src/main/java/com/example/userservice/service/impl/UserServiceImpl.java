package com.example.userservice.service.impl;

import com.example.userservice.entity.User;
import com.example.userservice.exceptions.DuplicateResourceException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        String emailId = user.getEmailId();
        if(Objects.nonNull(getUserByEmailId(emailId))) {
            throw new DuplicateResourceException("User with requested email Id already exists");
        }
        return userRepository.save(user);
    }

    private User getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

}
