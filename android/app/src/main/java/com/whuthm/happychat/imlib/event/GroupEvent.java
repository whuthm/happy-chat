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

    public static class ChangedEvent extends GroupEvent {

        public ChangedEvent(Group group) {
            super(group);
        }
    }

    public interface Poster extends EventPoster {

        void postGroupChanged(Group group);
    }

}
