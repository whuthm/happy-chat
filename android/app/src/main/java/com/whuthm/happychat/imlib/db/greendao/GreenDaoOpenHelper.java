package com.whuthm.happychat.imlib.db.greendao;

import android.content.Context;

import com.whuthm.happychat.imlib.db.IConversationDao;
import com.whuthm.happychat.imlib.db.IGroupDao;
import com.whuthm.happychat.imlib.db.IMessageDao;
import com.whuthm.happychat.imlib.db.IOpenHelper;
import com.whuthm.happychat.imlib.db.IUserDao;
import com.whuthm.happychat.imlib.model.DaoMaster;

public class GreenDaoOpenHelper implements IOpenHelper {

    private static final String DB_NAME_PREFIX = "im_";
    private static final String DB_NAME_SUFFIX = ".db";
    private static final int DB_VERSION = 1;

    private final DaoMaster.OpenHelper openHelper;

    public GreenDaoOpenHelper(Context context, String userId) {
        this.openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME_PREFIX + userId + DB_NAME_SUFFIX, null);
    }

    DaoMaster getReadableDaoMaster() {
        return new DaoMaster(openHelper.getReadableDb());
    }

    DaoMaster getWritableDaoMaster() {
        return new DaoMaster(openHelper.getWritableDb());
    }

    @Override
    public IMessageDao getMessageDao() {
        return null;
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
