package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.service.connection.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ChatManager {

    private Map<String, Connection> connectionsByUserId = new ConcurrentHashMap<>();

    public void receiveMessage(MessageProtos.MessageBean messageBean) {

    }

    public void sendMessage(MessageProtos.MessageBean messageBean) {

    }


}
