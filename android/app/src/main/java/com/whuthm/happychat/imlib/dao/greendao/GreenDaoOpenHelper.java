package com.whuthm.happychat.imlib.dao.greendao;

import android.content.Context;

import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.dao.IGroupDao;
import com.whuthm.happychat.imlib.dao.IMDaoFactory;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.dao.IUserDao;
import com.whuthm.happychat.imlib.model.DaoMaster;

public class GreenDaoOpenHelper implements IMDaoFactory {
    
    private static final String DB_NAME_PREFIX = "im_";
    private static final String DB_NAME_SUFFIX = ".db";
    private static final int DB_VERSION = 1;
    
    private final DaoMaster.OpenHelper openHelper;
    
    private final IMessageDao messageDao;
    private final IConversationDao conversationDao;
    private final IGroupDao groupDao;
    private final IUserDao userDao;
    
    public GreenDaoOpenHelper(Context context, String userId) {
        this.openHelper = new DaoMaster.DevOpenHelper(context,
                DB_NAME_PREFIX + userId + DB_NAME_SUFFIX, null);
        
        this.messageDao = new MessageDaoImpl(this);
        this.conversationDao = new ConversationDaoImpl(this);
        this.groupDao = new GroupDaoImpl(this);
        this.userDao = new UserDaoImpl(this);
    }
    
    DaoMaster getReadableDaoMaster() {
        return new DaoMaster(openHelper.getReadableDb());
    }
    
    DaoMaster getWritableDaoMaster() {
        return new DaoMaster(openHelper.getWritableDb());
    }
    
    @Override
    public IMessageDao getMessageDao() {
        return this.messageDao;
    }
    
    @Override
    public IConversationDao getConversationDao() {
        return this.conversationDao;
    }
    
    @Override
    public IGroupDao getGroupDao() {
        return groupDao;
    }
    
    @Override
    public IUserDao getUserDao() {
        return userDao;
    }
}
