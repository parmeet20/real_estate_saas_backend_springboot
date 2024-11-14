package com.estate.est.dto;

import com.estate.est.entities.User;
import com.estate.est.entities.enums.PropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyDto {
    private Long id;
    @NotNull
    private PropertyType propertyType;
    @NotNull
    private String title;
    @NotNull
    private User owner;
    private String description;
    @NotNull
    private String city;
    @NotNull
    private String country;
    @NotNull
    private String address;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    @NotNull
    private List<String> utilities = new ArrayList<>();
    @NotNull
    private List<String> images = new ArrayList<>();
    @NotNull
    private Boolean petAllowed;
    @NotNull
    private Boolean smokingAllowed;
    @NotNull
    private Integer price;
    @NotNull
    private Integer area;
    @NotNull
    private Integer busDistance;
    @NotNull
    private Integer schoolDistance;
    @NotNull
    private Integer bathroom;
    @NotNull
    private Integer bedroom;
}
