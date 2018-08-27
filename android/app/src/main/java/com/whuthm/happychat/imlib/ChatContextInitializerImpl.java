package com.whuthm.happychat.imlib;

class ChatContextInitializerImpl implements ChatContext.Initializer {
    @Override
    public void initialize(ChatContext chatContext) {
        MessageManager messageManager = new MessageManager(chatContext);
        ConnectionManager connectionManager = new ConnectionManager(chatContext);
        connectionManager.setMessageReceiver(messageManager);
        chatContext.registerService(MessageService.class, messageManager);
        chatContext.registerService(ConversationServiceV2.class, new ConversationManager(chatContext));
        chatContext.registerService(ConnectionService.class, connectionManager);
    }
}
