package com.estate.est.controllers;

import com.estate.est.dto.GetUserDto;
import com.estate.est.service.implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/getuser/{userId}")
    public ResponseEntity<GetUserDto> findUserById(@PathVariable("userId")Long userId)throws Exception{
        ;return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<GetUserDto> updateUser(@RequestHeader("Authorization")String jwt, @RequestBody GetUserDto getUserDto)throws Exception{
        return new ResponseEntity<>(userService.updateUser(jwt,getUserDto),HttpStatus.CREATED);
    }
}
