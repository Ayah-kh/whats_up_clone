package com.ayah.whatsappclone.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageRequest messageRequest) {
        messageService.saveMessage(messageRequest);
    }

    @PostMapping(value = "/upload-media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadMedia(@RequestParam("chatId") String chatId,
                            @RequestParam("file") MultipartFile file,
                            Authentication authentication) {

        messageService.uploadMediaMessage(chatId, file, authentication);

    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessagesToSeen(@RequestParam("chat-id") String chatId,
                                  Authentication authentication) {
        messageService.setMessagesToSeen(chatId, authentication);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable String chatId) {
        return ResponseEntity.ok(messageService.findChatMessage(chatId));
    }
}
