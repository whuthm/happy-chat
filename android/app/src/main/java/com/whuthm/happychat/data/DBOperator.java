package com.whuthm.happychat.data;

import android.content.Context;

import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationDao;
import com.whuthm.happychat.imlib.model.DaoMaster;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Group;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageDao;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 数据库操作封装类
 *
 * Created by tanwei on 2018/7/19.
 */

public class DBOperator {
    
    private static final String DB_NAME = "db";
    
    private Context context;
    
    private DaoMaster.DevOpenHelper mHelper;
    
    private DBOperator(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }
    
    private static DBOperator sInstance;
    
    public static void init(Context context) {
        sInstance = new DBOperator(context);
    }
    
    public static void addConversation(Conversation conversation) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getConversationDao().insertOrReplace(conversation);
        
        session.clear();
    }
    
    public static void delConversation(Conversation conversation) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getConversationDao().delete(conversation);
        session.clear();
    }
    
    public static List<Conversation> getConversations(int count) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        queryBuilder.orderDesc(ConversationDao.Properties.CreateTime).limit(count);
        List<Conversation> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
    public static List<Conversation> getConversations() {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        queryBuilder.orderDesc(ConversationDao.Properties.CreateTime);
        List<Conversation> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
    public static void delConversation(Message message) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getMessageDao().delete(message);
        session.clear();
    }
    
    public static void addMessage(Message message) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getMessageDao().insertOrReplace(message);
        
        session.clear();
    }
    
    public static List<Message> getMessages(String conversationId, int count) {
        
        return getMessages(conversationId, count, 0);
    }
    
    public static List<Message> getMessages(String conversationId, int count, int start) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Message> queryBuilder = session.getMessageDao().queryBuilder();
        queryBuilder.where(MessageDao.Properties.To.eq(conversationId)).limit(count)
                .orderDesc(MessageDao.Properties.SendTime).offset(start);
        List<Message> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
    public static void addGroups(List<Group> list) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getGroupDao().insertInTx(list);
        session.clear();
    }
    
    public static List<Group> getGroups() {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        List<Group> list = session.getGroupDao().queryBuilder().list();
        session.clear();
        return list;
    }
    
    public static void addUsers(List<User> list) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getUserDao().insertInTx(list);
        session.clear();
    }
    
    public static List<User> getUsersByIds(List<String> idList) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        List<User> list = session.getUserDao().queryBuilder()
                .where(UserDao.Properties.Id.in(idList)).list();
        session.clear();
        return list;
    }
}
