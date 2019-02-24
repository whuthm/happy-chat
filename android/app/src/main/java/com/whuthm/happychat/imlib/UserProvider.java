package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.User;

public interface UserProvider {

    User getUser(String id);

    interface Aware {

        void setUserProvider(UserProvider userProvider);

    }

}
