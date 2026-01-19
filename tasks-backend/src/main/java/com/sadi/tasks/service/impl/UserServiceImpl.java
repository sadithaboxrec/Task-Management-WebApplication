package com.sadi.tasks.service.impl;

import com.sadi.tasks.dto.Response;
import com.sadi.tasks.dto.UserRequest;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Role;
import com.sadi.tasks.exceptions.BadRequestException;
import com.sadi.tasks.repo.UserRepo;
import com.sadi.tasks.security.JwtUtils;
import com.sadi.tasks.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("Sign Up User");
        Optional<User> registereduser = userRepo.findByUsername(userRequest.getUsername());

        if (registereduser.isPresent()) {
            throw new BadRequestException("Username is already in use");
        }

        User user = new User();
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        user.setRole(Role.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .build();
    }

    @Override
    public Response<?> login(UserRequest userRequest) {
        log.info("Login User");

        User user=userRepo.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong Password");
        }

        String token= jwtUtils.generateToken(user.getUsername());

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login is success")
                .data(token)
                .build();

    }

    @Override
    public User getCurrentLoggedUser() {

        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).orElseThrow(() -> new BadRequestException("User Not Found"));

    }
}
