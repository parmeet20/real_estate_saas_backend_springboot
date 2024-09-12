package com.estate.est.controllers;

import com.estate.est.dto.ApiResponse;
import com.estate.est.dto.GetUserDto;
import com.estate.est.dto.LoginDto;
import com.estate.est.dto.RegisterDto;
import com.estate.est.service.implementations.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;
    @PostMapping("/signup")
    public ResponseEntity<GetUserDto> createUserHandler(@Valid @RequestBody RegisterDto userData)throws Exception{
        return new ResponseEntity<>(authService.registerUser(userData), HttpStatus.CREATED);
    }
    @PostMapping("/signing")
    public ResponseEntity<ApiResponse> loginUserHandler(@RequestBody LoginDto userData) throws Exception{
        return new ResponseEntity<>(new ApiResponse(authService.loginUser(userData),"true"),HttpStatus.OK);
    }
    @GetMapping("/getemail")
    public ResponseEntity<GetUserDto> getEmailHandler(@RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(authService.findUserProfile(jwt),HttpStatus.OK);
    }
}
