package com.estate.est.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUserDto {
    private Long id;
    @Email
    private String email;
    @NotNull
    private String profileImage;
    @NotNull
    private String contactNumber;
}
