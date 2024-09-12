package com.estate.est.entities;

import com.estate.est.entities.enums.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;
    private String title;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String description;
    private String city;
    private String address;
    private String latitude;
    private String longitude;
    @ElementCollection
    private List<String> utilities = new ArrayList<>();
    @ElementCollection
    private List<String> images = new ArrayList<>();
    private Boolean petAllowed;
    private Boolean smokingAllowed;
    private Integer price;
    private Integer area;
    private Integer bathroom;
    private Integer bedroom;
    private Integer busDistance;
    private Integer schoolDistance;
    @ManyToMany
    private List<User> usersBookmarks = new ArrayList<>();
    @ManyToMany
    private List<User> bookedBy = new ArrayList<>();
}