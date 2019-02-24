package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.api.ApiService;
import com.whuthm.happychat.imlib.dao.IGroupDao;
import com.whuthm.happychat.imlib.event.GroupEvent;
import com.whuthm.happychat.imlib.model.Group;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

class GroupManager extends AbstractIMService implements GroupService, GroupProvider.Aware{

    private final GroupEvent.Poster poster;
    private final Scheduler diskScheduler;
    private final ApiService apiService;
    private final IGroupDao dao;

    private GroupProvider groupProvider;

    GroupManager(IMContext chatContext, GroupEvent.Poster poster, Scheduler diskScheduler, ApiService apiService, IGroupDao dao) {
        super(chatContext);
        this.poster = poster;
        this.diskScheduler = diskScheduler;
        this.apiService = apiService;
        this.dao = dao;
    }

    @Override
    public void setGroupProvider(GroupProvider groupProvider) {
        this.groupProvider = groupProvider;
    }

    @Override
    public Group getGroup(String id) {
        final GroupProvider groupProvider = this.groupProvider;
        return groupProvider != null ? groupProvider.getGroup(id) : null;
    }

    @Override
    public Observable<Group> getGroupFromDisk(String id) {
        return null;
    }

    @Override
    public Observable<Group> getGroupFromServer(String id) {
        return null;
    }
}
