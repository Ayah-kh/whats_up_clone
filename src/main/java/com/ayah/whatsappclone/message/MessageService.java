package com.ayah.whatsappclone.message;

import com.ayah.whatsappclone.chat.Chat;
import com.ayah.whatsappclone.chat.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    final private MessageRepository messageRepository;
    final private ChatRepository chatRepository;
    final private MessageMapper mapper;

    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat Not Found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(message.getReceiverId());
        message.setMessageType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        // TODO: 21/02/2026 send Notification
    }

    public List<MessageResponse> findChatMessage(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication) {

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat Not Found"));

        final String recipientId = getRecipientId(chat, authentication);
        messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);

        // TODO: 21/02/2026 send notification
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat Not Found"));

        final String senderId = getSenderId(chat, authentication);
        final String recipientId = getRecipientId(chat, authentication);
    }

    private String getSenderId(Chat chat, Authentication authentication) {
        return null;
    }

    private String getRecipientId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}
