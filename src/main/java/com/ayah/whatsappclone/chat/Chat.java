package com.ayah.whatsappclone.chat;

import com.ayah.whatsappclone.common.BaseAuditingEntity;
import com.ayah.whatsappclone.message.Message;
import com.ayah.whatsappclone.message.MessageState;
import com.ayah.whatsappclone.message.MessageType;
import com.ayah.whatsappclone.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chats")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
        query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :senderId OR c.recipient.id = :recipientId ORDER BY createdDate DESC")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER_ID,
        query = """
                SELECT DISTINCT c FROM Chat c 
                WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId) 
                OR (c.sender.id = :recipientId AND c.recipient.id = :senderId) 
                ORDER BY createdDate DESC""")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "sender_id")
    private User sender;

    @JoinColumn(name = "recipient_id")
    private User recipient;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId) {
        if (recipient.getId().equals(senderId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        }

        return recipient.getFirstName() + " " + recipient.getLastName();
    }

    @Transient
    public Long getUnreadMessage(final String senderId) {
        return messages.stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m -> MessageState.SENT == m.getState())
                .count();
    }

    @Transient
    public String getLastMessage() {
        if (messages != null && !messages.isEmpty()) {
            if (messages.get(0).getMessageType() != MessageType.TEXT) {
                return "Attachemnt";
            }
            return messages.get(0).getContent();
        }

        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if (messages != null && !messages.isEmpty()) {

            return messages.get(0).getCreatedDate();
        }

        return null;
    }
}
