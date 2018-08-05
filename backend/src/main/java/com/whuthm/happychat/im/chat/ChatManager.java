package com.whuthm.happychat.im.chat;

import com.whuthm.happychat.im.connection.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private Map<String, Connection> connectionsByUserId = new ConcurrentHashMap<>();




}
