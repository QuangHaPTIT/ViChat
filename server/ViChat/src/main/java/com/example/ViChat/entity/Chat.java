package com.example.ViChat.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chats")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String chatName;
    String chatImage;

    @Column(name = "is_group")
    boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "created_by")
    User createdBy;

    @ManyToMany
    @JoinTable(name = "admin_chats")
    Set<User> admins = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_chats")
    Set<User> users = new HashSet<>();

    @OneToMany
    List<Message> messages = new ArrayList<>();
}
