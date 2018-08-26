package com.whuthm.happychat.imlib.db.greendao;

import com.whuthm.happychat.imlib.db.IMessageDao;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageDao;
import com.whuthm.happychat.imlib.vo.LoadDataDirection;

import java.util.List;

class MessageDaoImpl extends AbstractGreenDao implements IMessageDao {

    MessageDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }

    @Override
    public List<Message> getHistoryMessages(String conversationId, long baseMessageId, LoadDataDirection direction, int count) {
        return null;
    }

    @Override
    public Message getMessage(long id) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        Message message = session.getMessageDao().load(id);
        session.clear();
        return message;
    }

    @Override
    public Message getMessageByUid(String uid) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        Message message = session.getMessageDao().queryBuilder().where(MessageDao.Properties.Uid.eq(uid)).unique();
        session.clear();
        return message;
    }

    @Override
    public void markMessagesOfConversationAsRead(String conversationId) {

    }

    @Override
    public void deleteMessage(long id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().deleteByKey(id);
        session.clear();
    }

    @Override
    public long insertMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().insert(message);
        session.clear();
        return message.getId();
    }

    @Override
    public void updateMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().update(message);
        session.clear();
    }
}
