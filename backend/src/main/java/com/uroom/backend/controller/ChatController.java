package com.uroom.backend.controller;

import com.uroom.backend.dto.*;
import com.uroom.backend.service.ChatService;
import com.uroom.backend.service.RoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RoomMemberService roomMemberService;

    @GetMapping("/room/{roomId}")
    public ChatRoomResponse getByRoomId(@PathVariable UUID roomId,
                                        @RequestParam UUID currentUserId) {
        return chatService.getChatByRoomId(roomId, currentUserId);
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageResponse> getMessages(@PathVariable UUID chatId,
                                            @RequestParam UUID currentUserId) {
        return chatService.getMessages(chatId, currentUserId);
    }

    @PostMapping("/{chatId}/messages")
    public MessageResponse sendMessage(@PathVariable UUID chatId,
                                      @RequestParam UUID senderId,
                                      @RequestParam String senderName,
                                      @RequestParam String senderAvatar,
                                      @RequestBody SendMessageRequest request) {
        return chatService.sendMessage(chatId, senderId, senderName, senderAvatar, request);
    }

    @GetMapping("/{chatId}/messages/{messageId}/attachments")
    public List<AttachmentResponse> getAttachments(@PathVariable UUID chatId,
                                                   @PathVariable UUID messageId) {
        return chatService.getAttachments(messageId);
    }

    @PostMapping("/room/{roomId}/chat")
    public ChatRoomResponse createChat(@PathVariable UUID roomId) {
        return chatService.createChat(roomId);
    }
}
