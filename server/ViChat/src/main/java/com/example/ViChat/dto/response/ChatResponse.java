package com.example.ViChat.dto.response;

import com.example.ViChat.entity.Message;
import com.example.ViChat.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {
    Integer id;

    @JsonProperty("chat_name")
    String chatName;
    @JsonProperty("chat_image")
    String chatImage;

    @JsonProperty("is_group")
    boolean isGroup;

    @JsonProperty("created_by")
    UserResponse createdBy;
    @JsonProperty("users")
    Set<UserResponse> users;
    List<Message> messages;
}
