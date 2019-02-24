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
import com.whuthm.happychat.app.MessageAppService;
import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.imlib.MessageService;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class MessageInputFragment extends BaseConversationFragment {

    private EditText input;

    private ColorfulTextView btnSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_message_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        input = view.findViewById(R.id.frag_chat_input);
        btnSend = view.findViewById(R.id.frag_chat_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = input.getText().toString();
                imContext.getService(MessageAppService.class)
                        .sendTextMessage(getConversationId(), getConversationType(), text)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<Message>() {
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
        });
    }
}
