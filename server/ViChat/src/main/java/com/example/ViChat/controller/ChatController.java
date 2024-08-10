package com.example.ViChat.controller;

import com.example.ViChat.dto.request.GroupChatRequest;
import com.example.ViChat.dto.request.SingleChatRequest;
import com.example.ViChat.dto.response.ApiResponse;
import com.example.ViChat.dto.response.ChatResponse;
import com.example.ViChat.dto.response.UserResponse;
import com.example.ViChat.entity.User;
import com.example.ViChat.mapper.ChatMapper;
import com.example.ViChat.service.ChatService;
import com.example.ViChat.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chats")
@Tag(name = "Chat Controller")
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    @PostMapping("/single")
    public ResponseEntity<ApiResponse> createChatHandle(@RequestBody @Valid SingleChatRequest singleChatRequest) {
        ChatResponse chatResponse = chatService.createChat(singleChatRequest.getUserId(), false);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Create chat success")
                .result(chatResponse)
                .build());

    }
    @PostMapping("/group")
    public ResponseEntity<ApiResponse> createGroupHandle(@RequestBody @Valid GroupChatRequest groupChatRequest) {
        ChatResponse chatResponse = chatService.createGroup(groupChatRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Create group success")
                .result(chatResponse)
                .build());
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse> findChatByIdHandle(@PathVariable("chatId") Integer chatId) {
        ChatResponse chatResponse = chatMapper.toChatResponse(chatService.findChatById(chatId));
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Get chat by id success")
                .result(chatResponse)
                .build());
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> findAllChatByUserIdHandle() {
        List<ChatResponse> chatResponses = chatService.findAllChatByUserId();
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Get all chat by user id success")
                .result(chatResponses)
                .build());
    }

    @PutMapping("/{chatId}/rename/{groupName}")
    public ResponseEntity<ApiResponse> renameChatHandle(@PathVariable("chatId") Integer chatId, @PathVariable("groupName") String groupName) {
        ChatResponse chatResponse = chatService.renameGroup(chatId, groupName);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Rename chat success")
                .result(chatResponse)
                .build());
    }


    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ApiResponse> addUserToGroupHandle(@PathVariable("chatId") Integer chatId, @PathVariable("userId") String userId) {
        ChatResponse chatResponse = chatService.addUserToGroup(userId, chatId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("add user to group success")
                .result(chatResponse)
                .build());
    }
    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<ApiResponse> removeUserFromGroupHandle(@PathVariable("chatId") Integer chatId, @PathVariable("userId") String userId) {
        ChatResponse chatResponse = chatService.removeFromGroup(chatId,userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("add user to group success")
                .result(chatResponse)
                .build());
    }
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandle(@PathVariable("chatId") Integer chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Delete chat success")
                .build());
    }
}
