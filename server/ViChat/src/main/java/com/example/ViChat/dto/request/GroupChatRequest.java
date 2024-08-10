package com.example.ViChat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupChatRequest {
    List<String> userIds;

    @JsonProperty("chat_name")
    String chatName;

    @JsonProperty("chat_image")
    String chatImage;
}
