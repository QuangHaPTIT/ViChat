package com.example.ViChat.dto.response;

import com.example.ViChat.entity.Chat;
import com.example.ViChat.entity.Media;
import com.example.ViChat.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {

    Integer id;

    String content;

    LocalDateTime timestamp;

    UserResponse user;

    ChatResponse chat;

    List<MediaResponse> medias;
}
