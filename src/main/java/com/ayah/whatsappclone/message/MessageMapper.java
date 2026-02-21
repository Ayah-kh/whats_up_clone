package com.ayah.whatsappclone.message;

import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .messageType(message.getMessageType())
                .messageState(message.getState())
                .createdAt(message.getCreatedDate())
                // TODO: 21/02/2026 read the media file
                .build();
    }
}
