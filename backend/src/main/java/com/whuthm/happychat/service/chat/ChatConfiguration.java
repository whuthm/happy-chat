package com.whuthm.happychat.service.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfiguration {

    @Bean
    ChatManager chatManager() {
        return new ChatManager();
    }

}
