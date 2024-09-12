package com.estate.est.service.implementations;

import com.estate.est.config.JwtProvider;
import com.estate.est.dto.CreatePropertyDto;
import com.estate.est.entities.Property;
import com.estate.est.entities.User;
import com.estate.est.exceptions.PropertyException;
import com.estate.est.exceptions.UserException;
import com.estate.est.repositories.PropertyRepository;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public List<Property> getAllProperties(int pageNumber, int pageSize, String field) {
        Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, field));
        Page<Property> pageProperty = propertyRepository.findAll(p);
        List<Property> propertyList= pageProperty.getContent();
        return propertyList;
    }

    @Override
    public CreatePropertyDto addProperty(Long userId, CreatePropertyDto propertyDto, String jwt) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        String email = jwtProvider.getEmailFromToken(jwt);
        if (userOptional.isEmpty()){
            throw new PropertyException("user not found");
        }
        if (!Objects.equals(userOptional.get().getEmail(), email)){
            throw new PropertyException("you cannot delete any other property listing");
        }
        User owner = userOptional.get();
        Property newProperty = new Property();
        newProperty.setOwner(owner);
        newProperty.setTitle(propertyDto.getTitle());
        newProperty.setDescription(propertyDto.getDescription());
        newProperty.setArea(propertyDto.getArea());
        newProperty.setBusDistance(propertyDto.getBusDistance());
        newProperty.setSchoolDistance(propertyDto.getSchoolDistance());
        newProperty.setCity(propertyDto.getCity());
        newProperty.setAddress(propertyDto.getAddress());
        newProperty.setSmokingAllowed(propertyDto.getSmokingAllowed());
        newProperty.setPetAllowed(propertyDto.getPetAllowed());
        newProperty.setPropertyType(propertyDto.getPropertyType());
        newProperty.setPrice(propertyDto.getPrice());
        newProperty.setLatitude(propertyDto.getLatitude());
        newProperty.setLongitude(propertyDto.getLongitude());
        newProperty.setImages(propertyDto.getImages());
        newProperty.setUtilities(propertyDto.getUtilities());
        newProperty.setBathroom(propertyDto.getBathroom());
        newProperty.setBedroom(propertyDto.getBedroom());
        propertyRepository.save(newProperty);

        return new CreatePropertyDto(
                newProperty.getId(),
                newProperty.getPropertyType(),
                newProperty.getTitle(),
                newProperty.getOwner(),
                newProperty.getDescription(),
                newProperty.getCity(),
                newProperty.getAddress(),
                newProperty.getLatitude(),
                newProperty.getLongitude(),
                newProperty.getUtilities(),
                newProperty.getImages(),
                newProperty.getPetAllowed(),
                newProperty.getSmokingAllowed(),
                newProperty.getPrice(),
                newProperty.getArea(),
                newProperty.getBusDistance(),
                newProperty.getSchoolDistance(),
                newProperty.getBathroom(),
                newProperty.getBedroom()
        );
    }


    @Override
    public Property getPropertyById(Long propertyId) throws Exception {
        Optional<Property> property = propertyRepository.findById(propertyId);
        if(property.isEmpty()){
            throw new PropertyException("property not found");
        }
        return property.get();
    }

    @Override
    public Property updateProperty(Long propertyId, CreatePropertyDto propertyDto, String jwt) throws Exception {
        Optional<Property> propertyExists = propertyRepository.findById(propertyId);
        String email = jwtProvider.getEmailFromToken(jwt);

        if (propertyExists.isEmpty()) {
            throw new PropertyException("Property not found");
        }
        if(!Objects.equals(propertyExists.get().getOwner().getEmail(), email)){
            throw new PropertyException("you cannot update this post");
        }

        Property existingProperty = propertyExists.get();
        existingProperty.setTitle(propertyDto.getTitle());
        existingProperty.setDescription(propertyDto.getDescription());
        existingProperty.setArea(propertyDto.getArea());
        existingProperty.setBusDistance(propertyDto.getBusDistance());
        existingProperty.setSchoolDistance(propertyDto.getSchoolDistance());
        existingProperty.setCity(propertyDto.getCity());
        existingProperty.setAddress(propertyDto.getAddress());
        existingProperty.setSmokingAllowed(propertyDto.getSmokingAllowed());
        existingProperty.setPetAllowed(propertyDto.getPetAllowed());
        existingProperty.setPropertyType(propertyDto.getPropertyType());
        existingProperty.setPrice(propertyDto.getPrice());
        existingProperty.setLatitude(propertyDto.getLatitude());
        existingProperty.setLongitude(propertyDto.getLongitude());
        existingProperty.setImages(propertyDto.getImages());
        existingProperty.setUtilities(propertyDto.getUtilities());

        propertyRepository.save(existingProperty);
        return existingProperty;
    }


    @Override
    public String deleteProperty(Long propertyId,String jwt)throws Exception {
        Optional<Property> propertyExixts = propertyRepository.findById(propertyId);
        if (propertyExixts.isEmpty()){
            throw new PropertyException("property not found");
        }
        Optional<User> deleteFromUser = userRepository.findById(propertyExixts.get().getOwner().getId());
        if(deleteFromUser.isEmpty()){
            throw new PropertyException("user not found");
        }
        String email = jwtProvider.getEmailFromToken(jwt);
        if (!Objects.equals(deleteFromUser.get().getEmail(), email)){
            throw new PropertyException("you cannot delete other user's property listing");
        }
        deleteFromUser.get().getMyListings().remove(propertyExixts.get());
        propertyRepository.deleteById(propertyId);
        return "Property deleted successfully";
    }
    @Override
    public List<Property> getAllUserPropertiesById(Long userId) throws Exception {
        // Check if the user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserException("User not found");
        }
        return propertyRepository.getAllUserProperties(userId);
    }

    @Override
    public List<User> allBookings(Long propertyId) throws Exception {
        Optional<Property> propertyExists = propertyRepository.findById(propertyId);
        if (propertyExists.isEmpty()){
            throw new PropertyException("property not found with id "+propertyId);
        }
        return propertyExists.get().getBookedBy();
    }
}
