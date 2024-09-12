package com.estate.est.service;

import com.estate.est.dto.CreatePropertyDto;
import com.estate.est.entities.Property;
import com.estate.est.entities.User;

import java.util.List;

public interface PropertyService {
    List<Property> getAllProperties(int pageNumber, int pageSize, String field);
    CreatePropertyDto addProperty(Long userId, CreatePropertyDto property, String jwt)throws Exception;
    Property getPropertyById(Long propertyId)throws Exception;
    Property updateProperty(Long propertyId,CreatePropertyDto property,String jwt)throws Exception;
    String deleteProperty(Long propertyId,String jwt)throws Exception;
    List<Property> getAllUserPropertiesById(Long userId)throws Exception;
    List<User> allBookings(Long propertyId)throws Exception;
}
