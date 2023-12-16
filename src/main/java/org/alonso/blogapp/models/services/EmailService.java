package org.alonso.blogapp.models.services;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
