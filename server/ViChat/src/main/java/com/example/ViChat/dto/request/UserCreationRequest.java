package com.example.ViChat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @JsonProperty("full_name")
    String fullName;

    @NotEmpty(message = "EMAIL_NOT_EMPTY")
    @Email(message = "EMAIL_INVALID")
    @JsonProperty("email")
    String email;

    @JsonProperty("profile_picture")
    String profilePicture;

    @Size(min = 6, message = "PASSWORD_INVALID")
    @NotBlank
    @JsonProperty("password")
    String password;

    @Size(min = 6, message = "PASSWORD_INVALID")
    @NotBlank
    @JsonProperty("retype_password")
    String retypePassword;

    @JsonProperty("date_of_birth")
    LocalDate dateOfBirth;
}
