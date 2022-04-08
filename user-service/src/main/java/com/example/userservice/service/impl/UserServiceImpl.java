package com.example.userservice.service.impl;

import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.exceptions.BadRequestException;
import com.example.userservice.exceptions.DuplicateResourceException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto saveUser(User user) {
        String emailId = user.getEmailId();
        if(Objects.nonNull(getUserByEmailId(emailId))) {
            log.error("User with requested email Id already exists");
            throw new DuplicateResourceException("User with requested email Id already exists");
        }
        user = userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user, userResponseDto);
        log.info("Successfully saved a user with userId: {}", userResponseDto.getUserId());
        return userResponseDto;
    }

    private User getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    @Override
    public UserResponseDto getUser(String userId) {
        User user = userRepository.findById(convertStringToUUID(userId))
                .orElseThrow(() -> {
                    log.error(buildUserDoesNotExistsExceptionString(userId));
                    throw new NotFoundException(buildUserDoesNotExistsExceptionString(userId));
                });
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user, userResponseDto);
        log.info("Successfully fetched user details with userId: {}", userId);
        return userResponseDto;
    }

    private UUID convertStringToUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch(IllegalArgumentException ex) {
            log.error("Invalid UUID string: {}", uuidString);
            throw new BadRequestException("Invalid UUID string: "+uuidString);
        }
    }

    @Override
    public UserResponseDto updateUser(String userId, User user) {
        UUID userIdUuid = convertStringToUUID(userId);
        Optional<User> optionalUser = userRepository.findById(userIdUuid);
        if(optionalUser.isEmpty()) {
            log.error(buildUserDoesNotExistsExceptionString(userId));
            throw new NotFoundException(buildUserDoesNotExistsExceptionString(userId));
        }
        user.setUserId(userIdUuid);
        if(!user.getEmailId().equalsIgnoreCase(optionalUser.get().getEmailId())) {
            log.warn("Once a user is created, emailId cannot be updated");
        }
        user.setEmailId(optionalUser.get().getEmailId());
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user, userResponseDto);
        log.info("Successfully updated user details for userId: {}", userId);
        return userResponseDto;
    }

    @Override
    public void deleteUser(String userId) {
        try {
            userRepository.deleteById(convertStringToUUID(userId));
            log.info("Successfully deleted user details with userId: {}", userId);
        } catch(EmptyResultDataAccessException ex) {
            log.error(buildUserDoesNotExistsExceptionString(userId));
            throw new NotFoundException(buildUserDoesNotExistsExceptionString(userId));
        }
    }

    private String buildUserDoesNotExistsExceptionString(String userId) {
        return "User with userId: "+userId+" does not exist";
    }

}
