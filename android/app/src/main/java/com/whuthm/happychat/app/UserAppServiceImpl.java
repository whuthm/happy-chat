package com.whuthm.happychat.app;

import com.whuthm.happychat.imlib.AbstractIMService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.UserService;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserInfo;

import io.reactivex.Observable;

class UserAppServiceImpl extends AbstractIMService implements UserAppService {

    protected UserAppServiceImpl(IMContext imContext) {
        super(imContext);
    }

    @Override
    public UserInfo getCurrentUserInfo() {
        final User user = getImContext().getService(UserService.class).getUser(getCurrentUserId());
        if (user != null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setGender(user.getGender());
            userInfo.setName(user.getName());
            userInfo.setPortraitUrl(user.getPortraitUrl());
            return userInfo;
        }
        return null;
    }

    @Override
    public Observable<User> getCurrentUserFromServer() {
        return getImContext().getService(UserService.class)
                .getUserFromServer(getCurrentUserId());
    }
}
