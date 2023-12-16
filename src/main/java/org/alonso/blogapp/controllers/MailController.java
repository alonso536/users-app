package org.alonso.blogapp.controllers;

import java.util.HashMap;
import java.util.Map;

import org.alonso.blogapp.models.dto.EmailDTO;
import org.alonso.blogapp.models.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> send(@RequestBody EmailDTO emailDTO) {
        Map<String, Object> response = new HashMap<>();
        emailService.sendEmail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getText());

        response.put("status", 200);
        response.put("message", "Correo enviado exitosamente");
        return ResponseEntity.status(200).body(response);
    }
}
