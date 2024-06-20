package com.entseeker.Hotel.service.interfac;

import com.entseeker.Hotel.dto.LoginRequest;
import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
