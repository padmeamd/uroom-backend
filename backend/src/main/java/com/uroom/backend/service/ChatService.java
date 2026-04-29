package com.uroom.backend.service;

import com.uroom.backend.domain.Chat;
import com.uroom.backend.domain.Message;
import com.uroom.backend.domain.MessageAttachment;
import com.uroom.backend.domain.MessageAttachment.AttachmentType;
import com.uroom.backend.domain.Room;
import com.uroom.backend.dto.*;
import com.uroom.backend.repository.ChatRepository;
import com.uroom.backend.repository.MessageAttachmentRepository;
import com.uroom.backend.repository.MessageRepository;
import com.uroom.backend.repository.RoomRepository;
import com.uroom.backend.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public ChatRoomResponse getChatByRoomId(UUID roomId, UUID currentUserId) {
        Chat chat = chatRepository.findByRoomId(roomId)
            .orElseGet(() -> chatRepository.save(new Chat(roomId)));
        
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        List<com.uroom.backend.domain.RoomMember> members = 
            roomMemberRepository.findByRoomIdAndStatus(roomId, com.uroom.backend.domain.RoomMember.MemberStatus.ACTIVE);
        
        List<String> avatars = members.stream()
            .map(m -> roomRepository.findById(roomId).map(r -> "placeholder").orElse(""))
            .limit(3)
            .collect(Collectors.toList());
        
        return new ChatRoomResponse(
            chat.getId(),
            roomId,
            room.getTitle(),
            room.getRoomType(),
            room.getCurrentMembers(),
            avatars,
            chat.getCreatedAt()
        );
    }

    public List<MessageResponse> getMessages(UUID chatId, UUID currentUserId) {
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId)
            .stream()
            .map(m -> toMessageResponse(m, currentUserId))
            .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponse sendMessage(UUID chatId, UUID senderId, String senderName, 
                                      String senderAvatar, SendMessageRequest request) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new IllegalArgumentException("Chat not found: " + chatId));
        
        Message message = new Message(chatId, senderId, senderName, senderAvatar, request.text());
        
        if (request.location() != null) {
            message.setLocation(
                request.location().lat(),
                request.location().lng(),
                request.location().label()
            );
        }
        
        Message saved = messageRepository.save(message);
        return toMessageResponse(saved, senderId);
    }

    @Transactional
    public void addAttachment(UUID messageId, String url, String name, AttachmentType type, 
                            String mimeType, long size) {
        MessageAttachment attachment = new MessageAttachment(messageId, url, name, type, mimeType, size);
        attachmentRepository.save(attachment);
    }

    public List<AttachmentResponse> getAttachments(UUID messageId) {
        return attachmentRepository.findByMessageId(messageId)
            .stream()
            .map(a -> new AttachmentResponse(
                a.getId(),
                a.getFileUrl(),
                a.getFileName(),
                a.getAttachmentType(),
                a.getMimeType(),
                a.getSize()
            ))
            .collect(Collectors.toList());
    }

    public ChatRoomResponse createChat(UUID roomId) {
        if (chatRepository.findByRoomId(roomId).isPresent()) {
            throw new IllegalStateException("Chat already exists for room: " + roomId);
        }
        
        Chat chat = new Chat(roomId);
        Chat saved = chatRepository.save(chat);
        
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        return new ChatRoomResponse(
            saved.getId(),
            roomId,
            room.getTitle(),
            room.getRoomType(),
            room.getCurrentMembers(),
            List.of(),
            saved.getCreatedAt()
        );
    }

    private MessageResponse toMessageResponse(Message message, UUID currentUserId) {
        List<AttachmentResponse> attachments = attachmentRepository.findByMessageId(message.getId())
            .stream()
            .map(a -> new AttachmentResponse(
                a.getId(),
                a.getFileUrl(),
                a.getFileName(),
                a.getAttachmentType(),
                a.getMimeType(),
                a.getSize()
            ))
            .collect(Collectors.toList());
        
        LocationData location = null;
        if (message.getLatitude() != null && message.getLongitude() != null) {
            location = new LocationData(
                message.getLatitude(),
                message.getLongitude(),
                message.getLocationLabel()
            );
        }
        
        return new MessageResponse(
            message.getId(),
            message.getSenderId(),
            message.getSenderName(),
            message.getSenderAvatar(),
            message.getText(),
            attachments.isEmpty() ? null : attachments,
            location,
            message.getSenderId().equals(currentUserId),
            message.getCreatedAt()
        );
    }
}
