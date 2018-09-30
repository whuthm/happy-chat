package com.whuthm.happychat.imlib.dao.greendao;

import com.whuthm.happychat.imlib.dao.IUserDao;
import com.whuthm.happychat.imlib.model.DaoMaster;
import com.whuthm.happychat.imlib.model.DaoSession;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserDao;

import java.util.List;

/**
 * Created by tanwei on 2018/9/29.
 */

public class UserDaoImpl extends AbstractGreenDao implements IUserDao {
    
    UserDaoImpl(GreenDaoOpenHelper openHelper) {
        super(openHelper);
    }
    
    public void addUsers(User... list) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getUserDao().insertInTx(list);
        session.clear();
    }
    
    public void addUsers(List<User> list) {
        DaoSession session = getOpenHelper().getWritableDaoMaster().newSession();
        session.getUserDao().insertInTx(list);
        session.clear();
    }
    
    public User getUsersById(String id) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        List<User> list = session.getUserDao().queryBuilder()
                .where(UserDao.Properties.Id.eq(id)).limit(1).list();
        session.clear();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<User> getUsersByIds(List<String> idList) {
        DaoSession session = getOpenHelper().getReadableDaoMaster().newSession();
        List<User> list = session.getUserDao().queryBuilder()
                .where(UserDao.Properties.Id.in(idList)).list();
        session.clear();
        return list;
    }
}
