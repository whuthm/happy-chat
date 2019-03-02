package com.whuthm.happychat.app;

import android.util.Log;

import com.whuthm.happychat.imlib.AbstractIMService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.UserService;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserInfo;
import com.whuthm.happychat.util.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

class UserAppServiceImpl extends AbstractIMService implements UserAppService {

    private final static String TAG = UserAppServiceImpl.class.getSimpleName();

    private final Map<String, User> users;
    private final Set<String> fetching;

    protected UserAppServiceImpl(IMContext imContext) {
        super(imContext);
        this.users = new ConcurrentHashMap<>();
        this.fetching = new HashSet<>();
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

    @Override
    public User getOrFetchUser(String userId) {
        final User user = users.get(userId);
        if (user == null && !StringUtils.isEmpty(userId)) {
            fetchUser(userId);
        }
        return user;
    }

    @Override
    public User getUser(String userId) {
        return users.get(userId);
    }

    private void fetchUser(final String id) {
        if (fetching.contains(id)) {
            return;
        }
        Log.i(TAG, "fetchUser: " + id);
        fetching.add(id);
        getImContext().getService(UserService.class)
                .getUserFromDisk(id)
                .onErrorResumeNext(getImContext().getService(UserService.class).getUserFromServer(id))
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User value) {
                        users.put(value.getId(), value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetching.remove(id);
                    }

                    @Override
                    public void onComplete() {
                        fetching.remove(id);
                    }
                });
    }

    @Subscribe
    public void onUserUpdatedEvent(UserEvent.ChangedEvent event) {
        users.put(event.getUser().getId(), event.getUser());
    }
}
