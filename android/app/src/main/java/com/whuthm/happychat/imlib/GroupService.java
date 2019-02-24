package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Group;

import io.reactivex.Observable;

/**
 * Created by huangming on 18/07/2018.
 */

public interface GroupService extends GroupProvider {

    Observable<Group> getGroupFromDisk(String id);

    Observable<Group> getGroupFromServer(String id);

}
