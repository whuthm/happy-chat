package com.whuthm.happychat.imlib;

class ChatContextInitializerImpl implements ChatContext.Initializer {
    @Override
    public void initialize(ChatContext chatContext) {
        ConnectionManager connectionManager = new ConnectionManager(chatContext);
        MessageManager messageManager = new MessageManager(chatContext, connectionManager);
        connectionManager.setMessagePacketHandler(messageManager);
        connectionManager.setIqPacketHandler(messageManager);
        chatContext.registerService(MessageService.class, messageManager);
        chatContext.registerService(ConversationService.class, new ConversationManager(chatContext));
        chatContext.registerService(ConnectionService.class, connectionManager);
    }
}
