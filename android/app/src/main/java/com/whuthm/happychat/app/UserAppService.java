package com.whuthm.happychat.app;

import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserInfo;

import io.reactivex.Observable;

public interface UserAppService {

    UserInfo getCurrentUserInfo();

    Observable<User> getCurrentUserFromServer();

}
