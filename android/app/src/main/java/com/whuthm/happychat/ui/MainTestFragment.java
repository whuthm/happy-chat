package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barran.lib.app.BaseFragment;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.imlib.ConnectionService;
import com.whuthm.happychat.imlib.MessageService;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainTestFragment extends ChatContextFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MessageService messageService = chatContext.getService(MessageService.class);
        final AuthenticationService authenticationService = applicationServiceContext.getService(AuthenticationService.class);
        final ConnectionService connectionService = chatContext.getService(ConnectionService.class);
        view.findViewById(R.id.btn_connect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectionService.connect();
                        //ConversationActivity.startConversation(getActivity(), "vs", "GroupChat");
                    }
                });

        view.findViewById(R.id.btn_disconnect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectionService.disconnect();
                    }
                });

        final String to = UUID.randomUUID().toString();
        view.findViewById(R.id.btn_send_packet)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = new Message();
                        message.setFrom(authenticationService.getAuthenticationUser().getUserId());
                        message.setTo(to);
                        message.setBody("{\"text\":\"test\"}");
                        message.setConversationType("SingleChat");
                        message.setType("txt");
                        message.setUid(UUID.randomUUID().toString());
                        messageService.sendMessage(message).subscribe(new Observer<Message>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

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

                    }
                });
    }
}
