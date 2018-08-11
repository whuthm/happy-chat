package com.whuthm.happychat.validation;

import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.domain.model.Message;
import org.springframework.util.StringUtils;

class MessageValidation implements Validation<Message> {
    @Override
    public boolean isValid(Message obj) {
        return obj != null
                && ConversationType.from(obj.getConversationType()) != null
                && !StringUtils.isEmpty(obj.getFrom())
                && !StringUtils.isEmpty(obj.getTo())
                && !StringUtils.isEmpty(obj.getType());
    }
}
