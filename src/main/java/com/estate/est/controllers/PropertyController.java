package com.estate.est.controllers;

import com.estate.est.dto.ApiResponse;
import com.estate.est.dto.CreatePropertyDto;
import com.estate.est.entities.Property;
import com.estate.est.service.EmailService;
import com.estate.est.service.implementations.PropertyServiceImpl;
import com.estate.est.service.implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/properties")
public class PropertyController {
    @Autowired
    private PropertyServiceImpl propertyService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private EmailService emailService;
    @GetMapping("/all")
    public ResponseEntity<List<Property>> getAllPropertiesHandler(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "query", defaultValue = "id", required = false) List<String> query) {

        List<Property> properties = propertyService.getAllProperties(pageNumber, pageSize, query);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
    @GetMapping("/{propertyId}")
    public ResponseEntity<Property>getPropertyByIdHandler(@PathVariable("propertyId")Long propertyId)throws Exception{
        return new ResponseEntity<>(propertyService.getPropertyById(propertyId), HttpStatus.OK);
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<Property>> getAllUserPropertyHandler(@PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(propertyService.getAllUserPropertiesById(userId),HttpStatus.OK);
    }
    @PutMapping("/bookmark/{propertyId}/{userId}")
    public ResponseEntity<ApiResponse>addBookmarkHandler(@RequestHeader("Authorization")String jwt,@PathVariable("propertyId")Long propertyId, @PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(new ApiResponse(userService.addBookmark(jwt, userId, propertyId),"true"),HttpStatus.CREATED);
    }
    @PutMapping("/book/{propertyId}/{userId}")
    public ResponseEntity<ApiResponse>bookProperty(@RequestHeader("Authorization")String jwt,@PathVariable("propertyId")Long propertyId, @PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(new ApiResponse(userService.bookProperty(jwt, userId, propertyId),"true"),HttpStatus.CREATED);
    }
    @PostMapping("/create/{userId}")
    public ResponseEntity<CreatePropertyDto> createPropertyHandler(@RequestHeader("Authorization")String jwt, @PathVariable("userId")Long userId, @RequestBody CreatePropertyDto property)throws Exception{
        return new ResponseEntity<>(propertyService.addProperty(userId,property,jwt),HttpStatus.CREATED);
    }
    @PutMapping("/update/{propertyId}")
    public ResponseEntity<Property> updatePropertyHandler(@RequestHeader("Authorization")String jwt, @PathVariable("propertyId")Long propertyId, @RequestBody CreatePropertyDto property)throws Exception{
        return new ResponseEntity<>(propertyService.updateProperty(propertyId, property,jwt),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<ApiResponse> deletePropertyHandler(@RequestHeader("Authorization")String jwt, @PathVariable("propertyId")Long propertyId)throws Exception{
        return new ResponseEntity<>(new ApiResponse(propertyService.deleteProperty(propertyId,jwt),"success"),HttpStatus.OK);
    }
}
