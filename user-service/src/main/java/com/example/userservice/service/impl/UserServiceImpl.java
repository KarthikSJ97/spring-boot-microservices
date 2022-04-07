package com.example.userservice.service.impl;

import com.example.userservice.entity.User;
import com.example.userservice.exceptions.BadRequestException;
import com.example.userservice.exceptions.DuplicateResourceException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

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

    @Override
    public User getUser(String userId) {
        return userRepository.findById(convertStringToUUID(userId))
                .orElseThrow(() -> new NotFoundException("User with userId: "+userId+" does not exist"));
    }

    private UUID convertStringToUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch(IllegalArgumentException ex) {
            throw new BadRequestException("Invalid UUID string: "+uuidString);
        }
    }

}
