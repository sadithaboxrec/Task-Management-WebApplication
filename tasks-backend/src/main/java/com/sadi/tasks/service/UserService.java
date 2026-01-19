package com.sadi.tasks.service;

import com.sadi.tasks.dto.Response;
import com.sadi.tasks.dto.UserRequest;
import com.sadi.tasks.entity.User;

public interface UserService {

    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedUser();

}
