package com.whuthm.happychat.imlib.event;

import com.whuthm.happychat.imlib.model.Group;

/**
 * Created by huangming on 2017/10/16.
 */

public class GroupEvent {

    private final Group group;
    GroupEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public static class AddedEvent extends GroupEvent {

        public AddedEvent(Group group) {
            super(group);
        }
    }

    public static class UpdatedEvent extends GroupEvent {

        public UpdatedEvent(Group group) {
            super(group);
        }
    }

}
