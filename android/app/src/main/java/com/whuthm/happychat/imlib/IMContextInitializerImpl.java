package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.api.ApiService;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.imlib.dao.IMDaoFactory;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.SafeEventBus;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

class IMContextInitializerImpl implements IMContext.Initializer {

    private final EventPoster eventPoster;
    private final IMDaoFactory daoFactory;

    IMContextInitializerImpl(IMDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.eventPoster = new SafeEventBus();
    }

    @Override
    public void initialize(IMContext chatContext) {
        final Scheduler diskScheduler = Schedulers.io();
        final ApiService apiService = RetrofitClient.api();
        ConnectionManager connectionManager = new ConnectionManager(chatContext, new ConnectionEventPoster(eventPoster));
        final PacketSender packetSender = connectionManager;
        MessageSender messageSender = new MessageSender(packetSender, Schedulers.io());
        final IMessageDao messageDao = daoFactory.getMessageDao();
        MessageManager messageManager = new MessageManager(chatContext, messageSender, new MessageEventPoster(eventPoster), messageDao, diskScheduler);
        MessagePacketReceiver messagePacketReceiver = new MessagePacketReceiver(chatContext.getConfiguration().getUserId(), messageManager);
        connectionManager.addIqPacketHandler(messageSender);
        connectionManager.addMessagePacketHandler(messagePacketReceiver);
        chatContext.registerService(MessageService.class, messageManager);
        chatContext.registerService(ConversationService.class, new ConversationManager(chatContext, new ConversationEventPoster(eventPoster), daoFactory.getConversationDao(), messageDao, diskScheduler));
        chatContext.registerService(ConnectionService.class, connectionManager);
        UserManager userManager = new UserManager(chatContext, apiService, daoFactory.getUserDao(), new UserEventPoster(eventPoster), diskScheduler);
        chatContext.registerService(UserService.class, userManager);

        GroupManager groupManager = new GroupManager(chatContext, new GroupEventPoster(eventPoster), diskScheduler, apiService, daoFactory.getGroupDao());
        groupManager.setGroupProvider(new GroupProviderImpl(groupManager));
        chatContext.registerService(GroupService.class, groupManager);
    }
}
