package com.whuthm.happychat.validation;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.domain.model.ConversationType;
import org.springframework.util.StringUtils;

class MessageBeanValidation implements Validation<MessageProtos.MessageBean> {

    @Override
    public boolean isValid(MessageProtos.MessageBean obj) {
        return obj != null && ConversationType.from(obj.getConversationType()) != null
                && !StringUtils.isEmpty(obj.getFrom())
                && !StringUtils.isEmpty(obj.getTo())
                && !StringUtils.isEmpty(obj.getId())
                && !StringUtils.isEmpty(obj.getType());
    }
}
