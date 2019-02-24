package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.User;

class UserEventPoster extends EventPosterWrapper implements UserEvent.Poster {
    public UserEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postUserChanged(User user) {
        post(new UserEvent.ChangedEvent(user));
    }
}
