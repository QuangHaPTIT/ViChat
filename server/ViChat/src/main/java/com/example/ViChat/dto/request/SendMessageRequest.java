package com.example.ViChat.dto.request;

import com.example.ViChat.entity.Media;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageRequest {

    @JsonProperty("user_id")
    String userId;

    @JsonProperty("chat_id")
    Integer chatId;


    String content;

    List<MediaRequest> medias;
}
