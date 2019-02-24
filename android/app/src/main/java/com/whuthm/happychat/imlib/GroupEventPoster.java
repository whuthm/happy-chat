package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.event.GroupEvent;
import com.whuthm.happychat.imlib.model.Group;

class GroupEventPoster extends EventPosterWrapper implements GroupEvent.Poster {
    public GroupEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postGroupChanged(Group group) {
        post(new GroupEvent.ChangedEvent(group));
    }
}
