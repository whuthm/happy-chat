package com.whuthm.happychat.imlib;

class ChatContextInitializerImpl implements ChatContext.Initializer {
    @Override
    public void initialize(ChatContext chatContext) {
        MessageManager messageManager = new MessageManager(chatContext);
        ConnectionManager connectionManager = new ConnectionManager(chatContext);
        connectionManager.setMessageReceiver(messageManager);
        chatContext.registerServiceProvider(MessageService.class, messageManager);
        chatContext.registerServiceProvider(ConversationServiceV2.class, new ConversationManager(chatContext));
        chatContext.registerServiceProvider(ConnectionService.class, connectionManager);
    }
}
