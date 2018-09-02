package com.whuthm.happychat.imlib.dao;

public interface IMDaoFactory {

    IMessageDao getMessageDao();

    IConversationDao getConversationDao();

    IGroupDao getGroupDao();

    IUserDao getUserDao();

}
