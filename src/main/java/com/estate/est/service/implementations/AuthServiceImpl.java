package com.estate.est.service.implementations;

import com.estate.est.config.JwtProvider;
import com.estate.est.dto.GetUserDto;
import com.estate.est.dto.LoginDto;
import com.estate.est.dto.RegisterDto;
import com.estate.est.entities.User;
import com.estate.est.exceptions.UserException;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public GetUserDto registerUser(RegisterDto userDetails) throws Exception {
        User userExists = userRepository.getByEmail(userDetails.getEmail());
        if(userExists!=null){
            throw new UserException("user already exists");
        }
        User newUser = new User();
        newUser.setEmail(userDetails.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        newUser.setContactNumber(userDetails.getContactNumber());
        newUser.setProfileImage(userDetails.getProfileImage());
        userRepository.save(newUser);
        return new GetUserDto(newUser.getId(),newUser.getEmail(),newUser.getProfileImage(),newUser.getContactNumber());
    }

    @Override
    public GetUserDto findUserProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.getByEmail(email);
        if(user == null){
            throw new UserException("user not found");
        }
        return new GetUserDto(user.getId(),user.getEmail(),user.getProfileImage(),user.getContactNumber());
    }

    @Override
    public GetUserDto getUserById(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserException("user not found with id "+ userId);
        }
        return new GetUserDto(userOptional.get().getId(),userOptional.get().getEmail(),userOptional.get().getProfileImage(),userOptional.get().getContactNumber());
    }

    @Override
    public GetUserDto updateUser(Long userId) throws Exception {
        return null;
    }

    @Override
    public String loginUser(LoginDto loginDetails) throws Exception {
        User userExists = userRepository.getByEmail(loginDetails.getEmail());
        if(userExists == null){
            throw new UserException("user not found");
        }
        if(!passwordEncoder.matches(loginDetails.getPassword(),userExists.getPassword())){
            throw new UserException("incorrect password");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userExists.getEmail(),userExists.getPassword());
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public List<User> serarchUser(String query) throws Exception {
        return List.of();
    }
}
