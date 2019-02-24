package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Group;
import com.whuthm.happychat.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

class GroupProviderImpl implements GroupProvider {

    private final Map<String, Group> groups = new HashMap<>();
    private final GroupService groupService;

    GroupProviderImpl(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Group getGroup(String id) {
        final Group group = groups.get(id);
        if (group == null && !StringUtils.isEmpty(id)) {
            fetchGroup(id);
        }
        return group;
    }


    private void fetchGroup(String id) {
        groupService
                .getGroupFromDisk(id)
                .onErrorResumeNext(groupService.getGroupFromServer(id))
                .subscribe(new DisposableObserver<Group>() {
                    @Override
                    public void onNext(Group value) {
                        groups.put(value.getId(), value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
