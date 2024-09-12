package com.estate.est.service;

import com.estate.est.dto.GetUserDto;

public interface UserService {
    GetUserDto getUserById(Long userId)throws Exception;
    String addBookmark(String jwt,Long userId,Long propertyId)throws Exception;
    String bookProperty(String jwt,Long userId,Long propertyId)throws Exception;
    GetUserDto updateUser(String jwt,GetUserDto getUserDto)throws Exception;
}
