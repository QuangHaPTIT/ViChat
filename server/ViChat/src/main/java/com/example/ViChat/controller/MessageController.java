package com.example.ViChat.controller;

import com.example.ViChat.dto.request.SendMessageRequest;
import com.example.ViChat.dto.response.ApiResponse;
import com.example.ViChat.dto.response.MessageResponse;
import com.example.ViChat.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/messages")
@Tag(name = "Message Controller")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> sendMessageHandle(@RequestBody @Valid SendMessageRequest sendMessageRequest) {
        MessageResponse messageResponse = messageService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Send message success")
                .result(messageResponse)
                .build());
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<ApiResponse> getChatMessagesHandle(@PathVariable("chatId") Integer chatId) {
        List<MessageResponse> messageResponses = messageService.getChatMessages(chatId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Get all message from chat")
                .result(messageResponses)
                .build());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandle(@PathVariable("messageId") Integer messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Delete message success")
                .build());
    }
}
