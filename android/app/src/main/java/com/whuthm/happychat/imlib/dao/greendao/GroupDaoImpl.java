package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IGroupDao;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.Group;
import com.whuthm.happychat.imlib.model.GroupDao;

import java.util.List;

/**
 * Created by tanwei on 2018/9/29.
 */

public class GroupDaoImpl extends AbstractGreenDao implements IGroupDao {
    
    GroupDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }
    
    public void addGroups(Group... list) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getGroupDao().insertInTx(list);
        session.clear();
    }
    
    public void addGroups(List<Group> list) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getGroupDao().insertInTx(list);
        session.clear();
    }
    
    public Group getGroupBy(String id) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        List<Group> list = session.getGroupDao().queryBuilder()
                .where(GroupDao.Properties.Id.eq(id)).limit(1).list();
        session.clear();
        return list.isEmpty() ? null : list.get(0);
    }
}
