package com.whuthm.happychat.imlib;

public interface ChatServiceProvider<T> {

     T provideService(ChatContext chatContext);

}
