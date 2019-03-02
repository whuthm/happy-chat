package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.model.MessageDao;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

class MessageDaoImpl extends AbstractGreenDao implements IMessageDao {

    MessageDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }

    @Override
    public List<Message> getHistoryMessages(HistoryMessagesRequest request) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        QueryBuilder<Message> queryBuilder = session.getMessageDao()
                .queryBuilder()
                .limit(request.getCount());

        if (request.isBackward()) {
            if (request.isAscending()) {
                queryBuilder.orderDesc(MessageDao.Properties.Id);
            } else {
                queryBuilder.orderAsc(MessageDao.Properties.Id);
            }
        } else {
            if (request.isAscending()) {
                queryBuilder.orderAsc(MessageDao.Properties.Id);
            } else {
                queryBuilder.orderDesc(MessageDao.Properties.Id);
            }
        }
        WhereCondition conversationIdCondition = MessageDao.Properties.ConversationId.eq(request.getConversationId());
        WhereCondition idCondition;
        if (request.isBackward()) {
            idCondition = MessageDao.Properties.Id.lt(request.getBaseMessageId());
        } else {
            idCondition = MessageDao.Properties.Id.gt(request.getBaseMessageId());
        }
        WhereCondition deletedCondition = MessageDao.Properties.Deleted.notEq(true);
        List<Message> messages = queryBuilder.where(conversationIdCondition, idCondition, deletedCondition).build().forCurrentThread().list();
        session.clear();
        return messages;
    }

    @Override
    public Message getMessageByUid(String uid) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        Message message = session.getMessageDao().queryBuilder().where(MessageDao.Properties.Uid.eq(uid)).unique();
        session.clear();
        return message;
    }

    @Override
    public Message getMessage(long id) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        Message message = session.getMessageDao().load(id);
        session.clear();
        return message;
    }

    @Override
    public Message getLatestMessage() {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        WhereCondition deletedCondition = MessageDao.Properties.Deleted.notEq(true);
        List<Message> messages = session.getMessageDao().queryBuilder().orderDesc(MessageDao.Properties.SendTime).limit(1).where(deletedCondition).build().forCurrentThread().list();
        session.clear();
        return messages != null && messages.size() > 0 ? messages.get(0) : null;
    }

    @Override
    public void markMessagesOfConversationAsRead(String conversationId) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        List<Message> messages = session.getMessageDao().queryBuilder().where(
                MessageDao.Properties.ReceivedStatus.lt(Message.ReceivedStatus.FLAG_READ),
                MessageDao.Properties.Direction.eq(Message.Direction.RECEIVE.name())).build().forCurrentThread().list();
        if (messages != null) {
            for (Message message : messages) {
                message.setReceivedStatus(new Message.ReceivedStatus(Message.ReceivedStatus.FLAG_READ));
            }
            session.getMessageDao().updateInTx(messages);
        }
        session.clear();
    }

    @Override
    public void markMessageAsDeleted(long id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        Message message = session.getMessageDao().load(id);
        if (message != null) {
            message.setDeleted(true);
            session.getMessageDao().update(message);
        }
        session.clear();
    }

    @Override
    public void insertMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().insert(message);
        Message messageInDb = getMessageByUid(message.getUid());
        message.setId(messageInDb.getId());
        session.clear();
    }

    @Override
    public void updateMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().update(message);
        session.clear();
    }

    @Override
    public int getUnreadCountOf(String conversationId) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        long count = session.getMessageDao().queryBuilder().where(
                MessageDao.Properties.ReceivedStatus.lt(Message.ReceivedStatus.FLAG_READ),
                MessageDao.Properties.Type.notIn(MessageBody.getUnCountedTypes()),
                MessageDao.Properties.Deleted.notEq(true),
                MessageDao.Properties.Direction.eq(Message.Direction.RECEIVE.name())).count();
        session.clear();
        return (int) count;
    }

}
