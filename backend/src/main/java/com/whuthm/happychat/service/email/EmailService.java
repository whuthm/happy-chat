package com.whuthm.happychat.service.email;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
