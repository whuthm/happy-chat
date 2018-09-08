package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.barran.lib.view.text.ColorfulTextView;
import com.whuthm.happychat.R;
import com.whuthm.happychat.imlib.MessageService;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

import java.util.UUID;

import io.reactivex.observers.DisposableObserver;

public class MessageInputFragment extends BaseConversationFragment {

    private EditText input;

    private ColorfulTextView btnSend;
    MessageService messageService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_message_input, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageService = chatContext.getService(MessageService.class);
        input = view.findViewById(R.id.frag_chat_input);
        btnSend = view.findViewById(R.id.frag_chat_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = input.getText().toString();

                Message message = new Message();
                message.setTo(getConversationId());
                TextMessageBody textMessageBody = new TextMessageBody();
                textMessageBody.setText(text);
                message.setBodyObject(textMessageBody);
                message.setType(MessageTag.TYPE_TXT);
                message.setConversationType(getConversationType());
                if (!TextUtils.isEmpty(text)) {
                    messageService.sendMessage(message).subscribe(new DisposableObserver<Message>() {
                        @Override
                        public void onNext(Message value) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                    input.setText("");
                }
            }
        });
    }
}
