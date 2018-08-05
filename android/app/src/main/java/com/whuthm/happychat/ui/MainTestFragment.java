package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barran.lib.app.BaseFragment;
import com.whuthm.happychat.R;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.ChatConnection;
import com.whuthm.happychat.util.PacketIdGenerator;

public class MainTestFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ChatConnection chatConnection = new ChatConnection();
        view.findViewById(R.id.btn_connect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            chatConnection.connect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        view.findViewById(R.id.btn_disconnect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            chatConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        view.findViewById(R.id.btn_send_packet)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            PacketProtos.Packet packet = PacketProtos.Packet
                                    .newBuilder()
                                    .setId(PacketIdGenerator.nextId())
                                    .setType(PacketProtos.Packet.Type.message)
                                    .build();
                            chatConnection.sendPacket(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
