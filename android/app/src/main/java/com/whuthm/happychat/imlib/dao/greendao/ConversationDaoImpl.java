package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationDao;
import com.whuthm.happychat.imlib.model.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by tanwei on 2018/9/29.
 */

public class ConversationDaoImpl extends AbstractGreenDao implements IConversationDao {
    
    ConversationDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }
    
    @Override
    public Conversation getConversation(String conversationId) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        queryBuilder.where(ConversationDao.Properties.Id.eq(conversationId)).limit(1);
        List<Conversation> list = queryBuilder.list();
        session.clear();
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }
    
    @Override
    public List<Conversation> getAllConversations() {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        queryBuilder.orderDesc(ConversationDao.Properties.LatestMessageTime);
        List<Conversation> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
    @Override
    public void deleteConversation(String conversationId) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getConversationDao().deleteByKey(conversationId);
        
        session.clear();
    }
    
    @Override
    public void insertOrUpdateConversation(Conversation conversation) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getConversationDao().insertOrReplace(conversation);
        
        session.clear();
    }
}
