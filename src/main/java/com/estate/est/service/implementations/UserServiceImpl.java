package com.estate.est.service.implementations;

import com.estate.est.config.JwtProvider;
import com.estate.est.dto.GetUserDto;
import com.estate.est.dto.NotificationDto;
import com.estate.est.entities.Notification;
import com.estate.est.entities.Property;
import com.estate.est.entities.User;
import com.estate.est.exceptions.PropertyException;
import com.estate.est.exceptions.UserException;
import com.estate.est.repositories.PropertyRepository;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.EmailService;
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
    @Autowired
    private NotificationServiceImpl notificationService;
    @Autowired
    private EmailService emailService;
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
    public String bookProperty(String jwt, Long userId, Long propertyId) throws Exception {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (propertyOptional.isEmpty()) {
            throw new PropertyException("Property with id " + propertyId + " not found");
        }

        if (userOptional.isEmpty()) {
            throw new UserException("User with id " + userId + " not found");
        }

        User user = userOptional.get();
        Property property = propertyOptional.get();

        // Check JWT access
        if (!Objects.equals(jwtProvider.getEmailFromToken(jwt), user.getEmail())) {
            throw new UserException("Access denied for booking property");
        }
        // Toggle booking state
        if (user.getBookings().contains(property)) {
            user.getBookings().remove(property);
            property.getUsersBookmarks().remove(user);
        } else {
            user.getBookings().add(property);
            property.getBookedBy().add(user);
            notificationService.createNotification(userId,new NotificationDto(property.getOwner(),"Property "+ property.getTitle()+" booked successfully"));
            notificationService.createNotification(property.getOwner().getId(),new NotificationDto(property.getOwner(),"Your "+property.getTitle()+" is recently booked by "+user.getEmail()+". You can contact with "+user.getEmail()+" via contact number at "+user.getContactNumber()));
                emailService.sendMail(
                        property.getOwner().getEmail(),
                        "New Booking from " + user.getEmail(),
                        "Hello " + property.getOwner().getEmail() + ", your " + property.getTitle() + " has been booked by " + user.getEmail() + ".\nThanks for using EstateWave. Have a nice day."
                );
        }
        userRepository.save(user);
        propertyRepository.save(property);

        return "Property with id " + propertyId + " is successfully booked by user with id " + userId;
    }


    @Override
    public GetUserDto updateUser(String jwt, GetUserDto getUserDto) throws Exception {
        User user = userRepository.getByEmail(jwtProvider.getEmailFromToken(jwt));
        if (user == null){
            throw new UserException("user not found with user id ");
        }
        user.setEmail(getUserDto.getEmail());
        user.setProfileImage(getUserDto.getProfileImage());
        user.setContactNumber(getUserDto.getContactNumber());
        userRepository.save(user);
        return new GetUserDto(user.getId(), user.getEmail(), user.getProfileImage(), user.getContactNumber());
    }
}
