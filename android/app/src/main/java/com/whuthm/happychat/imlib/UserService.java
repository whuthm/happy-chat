package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.User;

import io.reactivex.Observable;

public interface UserService extends UserProvider {

    Observable<User> getUserFromDisk(String id);

    Observable<User> getUserFromServer(String id);

    User getUser(String id);

}
