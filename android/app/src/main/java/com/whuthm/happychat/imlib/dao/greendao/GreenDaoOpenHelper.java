package com.whuthm.happychat.imlib.dao.greendao;

import android.content.Context;

import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.dao.IGroupDao;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.dao.IMDaoFactory;
import com.whuthm.happychat.imlib.dao.IUserDao;
import com.whuthm.happychat.imlib.model.DaoMaster;
import com.whuthm.happychat.imlib.model.MessageDao;

public class GreenDaoOpenHelper implements IMDaoFactory {

    private static final String DB_NAME_PREFIX = "im_";
    private static final String DB_NAME_SUFFIX = ".db";
    private static final int DB_VERSION = 1;

    private final DaoMaster.OpenHelper openHelper;

    private final IMessageDao messageDao;

    public GreenDaoOpenHelper(Context context, String userId) {
        this.openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME_PREFIX + userId + DB_NAME_SUFFIX, null);

        this.messageDao = new MessageDaoImpl(this);
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
        return null;
    }

    @Override
    public IGroupDao getGroupDao() {
        return null;
    }

    @Override
    public IUserDao getUserDao() {
        return null;
    }
}
