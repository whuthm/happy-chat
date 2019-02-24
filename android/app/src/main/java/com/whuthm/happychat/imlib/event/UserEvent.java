package com.whuthm.happychat.imlib.event;

import com.whuthm.happychat.imlib.model.User;

public class UserEvent {

    public static final class ChangedEvent {
        final User user;

        public ChangedEvent(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public interface Poster extends EventPoster {
        void postUserChanged(User user);
    }


}
