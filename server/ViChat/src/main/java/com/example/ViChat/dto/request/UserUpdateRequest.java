package com.example.ViChat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
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
public class UserUpdateRequest {
    @JsonProperty("full_name")
    String fullName;

    @Email(message = "EMAIL_INVALID")
    @JsonProperty("email")
    String email;

    @JsonProperty("profile_picture")
    String profilePicture;

    @JsonProperty("password")
    String password;

    @JsonProperty("new_password")
    String newPassword;

    @JsonProperty("retype_new_password")
    String retypeNewPassword;

    @JsonProperty("date_of_birth")
    LocalDate dateOfBirth;

}
