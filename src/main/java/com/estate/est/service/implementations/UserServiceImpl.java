package com.estate.est.service.implementations;

import com.estate.est.config.JwtProvider;
import com.estate.est.dto.GetUserDto;
import com.estate.est.entities.Property;
import com.estate.est.entities.User;
import com.estate.est.exceptions.PropertyException;
import com.estate.est.exceptions.UserException;
import com.estate.est.repositories.PropertyRepository;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public GetUserDto getUserById(Long userId) throws Exception {
        Optional<User> userExists = userRepository.findById(userId);
        if (userExists.isEmpty()){
            throw new UserException("user with id "+ userId+" not found");
        }
        return new GetUserDto(userExists.get().getId(),userExists.get().getEmail(),userExists.get().getProfileImage(),userExists.get().getContactNumber());
    }

    @Override
    public String addBookmark(String jwt,Long userId, Long propertyId) throws Exception {
        Optional<Property> propertyExists = propertyRepository.findById(propertyId);
        Optional<User> userExists = userRepository.findById(userId);
        if (propertyExists.isEmpty()){
            throw new PropertyException("property with id "+ propertyId+" not found");
        }
        if (userExists.isEmpty()){
            throw new UserException("user with id "+ userId+" not found");
        }
        if (!Objects.equals(jwtProvider.getEmailFromToken(jwt), userExists.get().getEmail())) {
            throw new UserException("you cannot bookmark for another user id");
        }
        if (userExists.get().getBookmarks().contains(propertyExists.get()) && propertyExists.get().getUsersBookmarks().contains(userExists.get())){
            userExists.get().getBookmarks().remove(propertyExists.get());
            propertyExists.get().getUsersBookmarks().remove(userExists.get());
        }else {
            userExists.get().getBookmarks().add(propertyExists.get());
            propertyExists.get().getUsersBookmarks().add(userExists.get());
        }
        userRepository.save(userExists.get());
        propertyRepository.save(propertyExists.get());
        return "property with id "+propertyId+" is successfully bookmarked by user "+userId;
    }

    @Override
    public String bookProperty(String jwt,Long userId, Long propertyId) throws Exception {
        Optional<Property> propertyExists = propertyRepository.findById(propertyId);
        Optional<User> userExists = userRepository.findById(userId);
        if (propertyExists.isEmpty()){
            throw new PropertyException("property with id "+ propertyId+" not found");
        }
        if (userExists.isEmpty()){
            throw new UserException("user with id "+ userId+" not found");
        }
        if (!Objects.equals(jwtProvider.getEmailFromToken(jwt), userExists.get().getEmail())){
            throw new UserException("access denied of booking property");
        }
        if (userExists.get().getBookings().contains(propertyExists.get())){
            userExists.get().getBookmarks().remove(propertyExists.get());
            propertyExists.get().getUsersBookmarks().remove(userExists.get());
        }else {
            userExists.get().getBookmarks().add(propertyExists.get());
            propertyExists.get().getUsersBookmarks().add(userExists.get());
        }
        propertyExists.get().getBookedBy().add(userExists.get());
        propertyRepository.save(propertyExists.get());
        return "property with id "+propertyId+" is successfully booked by user with id "+userId;
    }

    @Override
    public GetUserDto updateUser(String jwt, GetUserDto getUserDto) throws Exception {
        User user = userRepository.getByEmail(jwtProvider.getEmailFromToken(jwt));
        if (user == null){
            throw new UserException("user not found with user id "+user.getId());
        }
        user.setEmail(getUserDto.getEmail());
        user.setProfileImage(getUserDto.getProfileImage());
        user.setContactNumber(getUserDto.getContactNumber());
        userRepository.save(user);
        return new GetUserDto(user.getId(), user.getEmail(), user.getProfileImage(), user.getContactNumber());
    }
}
