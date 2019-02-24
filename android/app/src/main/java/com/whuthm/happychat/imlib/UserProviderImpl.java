package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

class UserProviderImpl implements UserProvider {

    private final Map<String, User> users;
    private final UserService userService;

    UserProviderImpl(UserService userService) {
        this.userService = userService;
        this.users = new HashMap<>();
    }

    @Override
    public User getUser(String id) {
        final User user = users.get(id);
        if (user == null && !StringUtils.isEmpty(id)) {
            fetchUser(id);
        }
        return user;
    }

    private void fetchUser(String id) {
        userService
                .getUserFromDisk(id)
                .onErrorResumeNext(userService.getUserFromServer(id))
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User value) {
                        users.put(value.getId(), value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
