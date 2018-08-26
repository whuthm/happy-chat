package com.whuthm.happychat.imlib.db;

public interface IOpenHelper {

    IMessageDao getMessageDao();

    IConversationDao getConversationDao();

    IGroupDao getGroupDao();

    IUserDao getUserDao();

}
