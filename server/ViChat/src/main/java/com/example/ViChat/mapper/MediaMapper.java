package com.example.ViChat.mapper;

import com.example.ViChat.dto.request.MediaRequest;
import com.example.ViChat.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MediaMapper {
    @Mapping(target = "message", ignore = true)
    Media toMedia(MediaRequest mediaRequest);
}
