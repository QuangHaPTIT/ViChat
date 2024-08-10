package com.example.ViChat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "email")

    String email;

    @Column(name = "fullname")
    String fullName;

    String password;

    @Column(name = "profile_picture")
    String profilePicture;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @ManyToMany
    @JoinTable(
            name = "user_roles"
    )
    Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    OtpCode otpCode;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(fullName, user.fullName) && Objects.equals(password, user.password) && Objects.equals(profilePicture, user.profilePicture) && Objects.equals(isActive, user.isActive) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, fullName, password, profilePicture, isActive, roles);
    }
}
