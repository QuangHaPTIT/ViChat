package com.example.ViChat.service;

import com.example.ViChat.dto.request.MediaRequest;
import com.example.ViChat.entity.Media;
import com.example.ViChat.entity.Message;

import java.util.List;

public interface MediaService {
    Media save(Message message, MediaRequest mediaRequest);
    List<Media> getAllMediaByMessageId(int messageId);
    void deleteMediaById(int mediaId);
}
