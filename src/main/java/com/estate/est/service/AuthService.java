package com.estate.est.service;

import com.estate.est.dto.GetUserDto;
import com.estate.est.dto.LoginDto;
import com.estate.est.dto.RegisterDto;
import com.estate.est.entities.User;

import java.util.List;

public interface AuthService {
    GetUserDto registerUser(RegisterDto userDetails)throws Exception;
    GetUserDto findUserProfile(String jwt)throws Exception;
    GetUserDto getUserById(Long userId)throws Exception;
    GetUserDto updateUser(Long userId)throws Exception;
    String  loginUser(LoginDto loginDetails)throws Exception;
    List<User> serarchUser(String query)throws Exception;
}
