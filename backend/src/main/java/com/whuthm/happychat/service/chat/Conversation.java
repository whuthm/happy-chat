package com.whuthm.happychat.service.chat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class Conversation {

    private final Set<String> users;

    public Conversation(Collection<String> users) {
        this.users = new HashSet<>(users);
    }

    public Conversation() {
        this.users = new HashSet<>();
    }

    void join(String userId) {
        users.add(userId);
    }

    void exit(String userId) {
        users.remove(userId);
    }


}
