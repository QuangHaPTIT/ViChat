package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.MediaRequest;
import com.example.ViChat.dto.request.SendMessageRequest;
import com.example.ViChat.dto.response.MessageResponse;
import com.example.ViChat.entity.Chat;
import com.example.ViChat.entity.Media;
import com.example.ViChat.entity.Message;
import com.example.ViChat.entity.User;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.mapper.MediaMapper;
import com.example.ViChat.mapper.MessageMapper;
import com.example.ViChat.repository.MessageRepository;
import com.example.ViChat.service.ChatService;
import com.example.ViChat.service.MediaService;
import com.example.ViChat.service.MessageService;
import com.example.ViChat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private final MediaService mediaService;

    @Override
    @Transactional
    public MessageResponse sendMessage(SendMessageRequest sendMessageRequest) {
        User user = userService.findUserById(sendMessageRequest.getUserId());
        Chat chat = chatService.findChatById(sendMessageRequest.getChatId());
        Message message = Message.builder()
                .chat(chat)
                .content(sendMessageRequest.getContent())
                .user(user)
                .build();
        message = messageRepository.save(message);
        if(sendMessageRequest.getMedias() != null) {
            Message finalMessage = message;
            List<Media> medias = sendMessageRequest.getMedias().stream().map(mediaRequest -> {
                return  mediaService.save(finalMessage, mediaRequest);
            }).collect(Collectors.toList());
        }
        message = messageRepository.findById(message.getId()).orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));
        return messageMapper.toMessageResponse(message);
    }

    @Override
    public List<MessageResponse> getChatMessages(Integer chatId) {
        User reqUser = userService.findUserProfile();
        Chat chat = chatService.findChatById(chatId);
        if(!chat.getUsers().contains(reqUser)) throw new AppException(ErrorCode.USER_NOT_MEMBER_GROUP);
        List<MessageResponse> messageResponses = messageRepository.findByChatId(chatId)
                .stream().map(message -> messageMapper.toMessageResponse(message)).collect(Collectors.toList());
        return messageResponses;
    }

    @Override
    public Message findMessageById(Integer messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));
        return message;
    }

    @Override
    @Transactional
    public void deleteMessage(Integer messageId) {
        User user = userService.findUserProfile();
        Message message = findMessageById(messageId);
        if(!message.getUser().getId().equals(user.getId())) throw new AppException(ErrorCode.UNAUTHORIZED_MESSAGE_DELETION);
        messageRepository.delete(message);
    }
}
