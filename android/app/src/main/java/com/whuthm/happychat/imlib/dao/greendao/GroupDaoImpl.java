package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IGroupDao;
import com.whuthm.happychat.imlib.dao.InsertOrUpdateStatus;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Group;

/**
 * Created by tanwei on 2018/9/29.
 */

public class GroupDaoImpl extends AbstractGreenDao implements IGroupDao {
    
    GroupDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }

    @Override
    public Group getGroup(String id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        Group group = session.getGroupDao().load(id);
        session.clear();
        return group;
    }

    @Override
    public void deleteMessage(String id) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getGroupDao().deleteByKey(id);
        session.clear();
    }

    @Override
    public InsertOrUpdateStatus insertOrUpdateMessage(Group group) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        final boolean inserted = session.insertOrReplace(group) >= 0;
        session.clear();
        return new InsertOrUpdateStatus(inserted, !inserted);
    }

}
