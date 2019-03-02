package com.whuthm.happychat.app;

import com.whuthm.happychat.imlib.UserProvider;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserInfo;

import io.reactivex.Observable;

public interface UserAppService extends UserProvider {

    UserInfo getCurrentUserInfo();

    Observable<User> getCurrentUserFromServer();

    User getOrFetchUser(String userId);

}
