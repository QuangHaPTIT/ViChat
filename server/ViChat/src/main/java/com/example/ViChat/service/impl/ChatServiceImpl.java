package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.GroupChatRequest;
import com.example.ViChat.dto.response.ChatResponse;
import com.example.ViChat.entity.Chat;
import com.example.ViChat.entity.User;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.mapper.ChatMapper;
import com.example.ViChat.repository.ChatRepository;
import com.example.ViChat.service.ChatService;
import com.example.ViChat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatMapper chatMapper;
    @Override
    @Transactional
    public ChatResponse createChat(String userId2, boolean isGroup) {
        User user = userService.findUserById(userId2);
        User userReq = userService.findUserProfile();
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, userReq);
        if(isChatExist != null) return chatMapper.toChatResponse(isChatExist);
        Set<User> users = new HashSet<>();
        users.add(user);
        users.add(userReq);
        Chat chat = Chat.builder()
                .createdBy(userReq)
                .users(users)
                .isGroup(isGroup)
                .build();
        chatRepository.save(chat);
        return chatMapper.toChatResponse(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) {
        Chat chat = findByIdChat(chatId);
        return chat;
    }

    @Override
    public List<ChatResponse> findAllChatByUserId() {
        User user = userService.findUserProfile();
        List<ChatResponse> chatResponses = chatRepository.findChatByUserId(user.getId())
                .stream().map(chat -> chatMapper.toChatResponse(chat)).collect(Collectors.toList());
        return chatResponses;
    }

    @Override
    @Transactional
    public ChatResponse createGroup(GroupChatRequest req) {
        User user = userService.findUserProfile();
        Chat group = Chat.builder()
                .isGroup(true)
                .chatImage(req.getChatImage())
                .chatName(req.getChatName())
                .admins(Set.of(user))
                .createdBy(user)
                .build();
        Set<User> users = new HashSet<>();
        users.add(user);
        for(String userId : req.getUserIds()) {
            User existingUser = userService.findUserById(userId);
            users.add(existingUser);
        }
        group.setUsers(users);
        group = chatRepository.save(group);
        log.info(group.toString());
        return chatMapper.toChatResponse(group);
    }

    @Override
    @Transactional
    public ChatResponse addUserToGroup(String userId, Integer chatId) {
        Chat chat = findByIdChat(chatId);
        User userReq = userService.findUserProfile();
        if(!chat.getAdmins().contains(userReq)) throw new AppException(ErrorCode.USER_NOT_AUTHORIZED_TO_ADD_USER_TO_GROUP);
        User user = userService.findUserById(userId);
        chat.getUsers().add(user);
        chatRepository.save(chat);
        return chatMapper.toChatResponse(chat);
    }

    @Override
    @Transactional
    public ChatResponse renameGroup(Integer chatId, String groupName) {
        Chat chat = findByIdChat(chatId);
        User userReq = userService.findUserProfile();
        if(!chat.getUsers().contains(userReq)) throw new AppException(ErrorCode.USER_NOT_MEMBER_GROUP);
        chat.setChatName(groupName);
        chatRepository.save(chat);
        return chatMapper.toChatResponse(chat);
    }

    @Override
    @Transactional
    public ChatResponse removeFromGroup(Integer chatId, String userId) {
        Chat chat = findByIdChat(chatId);
        User user = userService.findUserById(userId);
        User userReq = userService.findUserProfile();
        if(chat.getAdmins().contains(userReq)) {
            chat.getUsers().remove(user);
        }else if(chat.getUsers().contains(userReq)) {
            if(userId.equals(userReq.getId())) chat.getUsers().remove(user);
        }
        chat = chatRepository.save(chat);
        return chatMapper.toChatResponse(chat);
    }

    @Override
    @Transactional
    public void deleteChat(Integer chatId) {
        Chat chat = findByIdChat(chatId);
        User userReq = userService.findUserProfile();
        if(!chat.getAdmins().contains(userReq)) throw new AppException(ErrorCode.USER_NOT_ADMIN_GROUP);
        chatRepository.delete(chat);
    }

    private Chat findByIdChat(Integer chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_EXIST));
        return chat;
    }
}
