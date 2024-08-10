package com.example.ViChat.service;

import com.example.ViChat.dto.request.SendMessageRequest;
import com.example.ViChat.dto.response.MessageResponse;
import com.example.ViChat.entity.Message;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(SendMessageRequest sendMessageRequest);
    List<MessageResponse> getChatMessages(Integer chatId);
    Message findMessageById(Integer messageId);
    void deleteMessage(Integer messageId);
}
