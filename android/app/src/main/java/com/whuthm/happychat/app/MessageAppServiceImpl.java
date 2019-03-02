package com.whuthm.happychat.app;

import android.text.TextUtils;

import com.whuthm.happychat.imlib.AbstractIMService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.MessageService;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

import io.reactivex.Observable;

class MessageAppServiceImpl extends AbstractIMService implements MessageAppService {

    protected MessageAppServiceImpl(IMContext imContext) {
        super(imContext);
    }

    @Override
    public Observable<Message> resendMessage(String messageUid) {
        return getImContext().getService(MessageService.class).resendMessage(messageUid);
    }

    @Override
    public Observable<Message> sendTextMessage(String conversationId, ConversationType conversationType, String text) {
        if (!TextUtils.isEmpty(text)) {
            Message message = new Message();
            message.setConversationId(conversationId);
            message.setConversationType(conversationType);
            message.setType(MessageTag.TYPE_TXT);
            TextMessageBody textMessageBody = new TextMessageBody();
            textMessageBody.setText(text);
            textMessageBody.setUserInfo(getImContext().getService(UserAppService.class).getCurrentUserInfo());
            message.setBodyObject(textMessageBody);
            return getImContext().getService(MessageService.class).sendMessage(message);
        } else {
            return Observable.error(new IllegalArgumentException("text is empty"));
        }
    }
}
