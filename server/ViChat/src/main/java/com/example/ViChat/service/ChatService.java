package com.example.ViChat.service;

import com.example.ViChat.dto.request.GroupChatRequest;
import com.example.ViChat.dto.response.ChatResponse;
import com.example.ViChat.entity.Chat;

import java.util.List;

public interface ChatService {
    ChatResponse createChat(String userId2, boolean isGroup);
    Chat findChatById(Integer chatId);
    List<ChatResponse> findAllChatByUserId();
    ChatResponse createGroup(GroupChatRequest req);
    ChatResponse addUserToGroup(String userId ,Integer chatId);
    ChatResponse renameGroup(Integer chatId, String groupName);
    ChatResponse removeFromGroup(Integer chatId, String userId);
    void deleteChat(Integer chatId);
}
