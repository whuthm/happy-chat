package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Group;

public interface GroupProvider {

    Group getGroup(String id);

    interface Aware {
        void setGroupProvider(GroupProvider groupProvider);
    }

}
