package com.example.ViChat.mapper;

import com.example.ViChat.dto.response.MessageResponse;
import com.example.ViChat.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);
}
