package com.estate.est.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String profileImage;
    @NotNull
    private String contactNumber;
    @JsonIgnore
    @ManyToMany(mappedBy = "usersBookmarks",cascade = CascadeType.ALL)
    private List<Property> bookmarks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Property> myListings = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "bookedBy",cascade = CascadeType.ALL)
    private List<Property> bookings = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user1",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Chat> chatsAsUser1 = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user2",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Chat> chatsAsUser2 = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}
