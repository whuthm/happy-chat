package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.Message;
import com.whuthm.happychat.domain.model.OfflineMessage;
import com.whuthm.happychat.domain.repository.OfflineMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class OfflineMessageManager {

    @Autowired
    OfflineMessageRepository offlineMessageRepository;

    void enqueue(Message message, String to) {
        OfflineMessage offlineMessage = new OfflineMessage();
        offlineMessage.setMessage(message);
        offlineMessage.setTo(to);
        offlineMessageRepository.save(offlineMessage);
    }

}
