package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Message;
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
            queryBuilder.orderDesc(MessageDao.Properties.Id);
        } else {
            queryBuilder.orderAsc(MessageDao.Properties.Id);
        }
        WhereCondition conversationIdCondition = MessageDao.Properties.ConversationId.eq(request.getConversationId());
        WhereCondition idCondition;
        if (request.isBackward()) {
            idCondition = MessageDao.Properties.Id.lt(request.getBaseMessageId());
        } else {
            idCondition = MessageDao.Properties.Id.gt(request.getBaseMessageId());
        }
        List<Message> messages = queryBuilder.where(conversationIdCondition, idCondition).build().list();
        session.clear();
        return messages;
    }

    @Override
    public Message getMessage(String id) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        Message message = session.getMessageDao().load(id);
        session.clear();
        return message;
    }

    @Override
    public void markMessagesOfConversationAsRead(String conversationId) {

    }

    @Override
    public void deleteMessage(String id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().deleteByKey(id);
        session.clear();
    }

    @Override
    public void insertMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().insert(message);
        session.clear();
    }

    @Override
    public void updateMessage(Message message) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getMessageDao().update(message);
        session.clear();
    }

}
