package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IUserDao;
import com.whuthm.happychat.imlib.dao.InsertOrUpdateStatus;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.User;

/**
 * Created by tanwei on 2018/9/29.
 */

public class UserDaoImpl extends AbstractGreenDao implements IUserDao {
    
    UserDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }

    @Override
    public User getUser(String id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        User user = session.getUserDao().load(id);
        session.clear();
        return user;
    }

    @Override
    public void deleteMessage(String id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getUserDao().deleteByKey(id);
        session.clear();
    }

    @Override
    public InsertOrUpdateStatus insertOrUpdateMessage(User user) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        final boolean inserted = session.insertOrReplace(user) >= 0;
        session.clear();
        return new InsertOrUpdateStatus(inserted, !inserted);
    }

}
