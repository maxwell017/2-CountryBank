package com.cbank.controllers;

import com.cbank.domain.Client;
import com.cbank.domain.message.Feedback;
import com.cbank.services.ClientService;
import com.cbank.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {
    private final MessageService messageService;
    private ClientService clientService;

    @ModelAttribute
    public Client client(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .flatMap(auth -> clientService.byUserId(auth.getName()))
                .orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> persist(@RequestBody Feedback feedback,
                                     @ModelAttribute Client client) {
        val toSave = Optional.ofNullable(client)
                .map(c -> Feedback.of(client.getName(), client.getEmail(), feedback.getBody()))
                .orElse(feedback);

        messageService.persist(toSave);
        return ResponseEntity.accepted().build();
    }


}
