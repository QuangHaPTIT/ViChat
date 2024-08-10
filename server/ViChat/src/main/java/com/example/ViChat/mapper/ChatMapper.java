package com.example.ViChat.mapper;

import com.example.ViChat.dto.response.ChatResponse;
import com.example.ViChat.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChatMapper {
    @Mapping(source = "group", target = "isGroup")
    ChatResponse toChatResponse(Chat chat);
}
