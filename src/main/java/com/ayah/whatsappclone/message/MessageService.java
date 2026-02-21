package com.ayah.whatsappclone.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    final private MessageRepository messageRepository;

    public void saveMessage(MessageRequest messageRequest) {

    }
}
