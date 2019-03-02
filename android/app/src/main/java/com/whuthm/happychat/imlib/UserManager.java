package com.whuthm.happychat.imlib;

import android.util.Log;

import com.whuthm.happychat.data.UserProtos;
import com.whuthm.happychat.data.api.ApiService;
import com.whuthm.happychat.data.api.ApiUtils;
import com.whuthm.happychat.imlib.dao.IUserDao;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

class UserManager extends AbstractIMService implements UserService, UserProvider.Aware {

    private static final String TAG = UserManager.class.getSimpleName();

    private final ApiService apiService;
    private final IUserDao dao;
    private final UserEvent.Poster poster;
    private final Scheduler diskScheduler;

    private UserProvider userProvider;


    UserManager(IMContext chatContext, ApiService apiService, IUserDao dao, UserEvent.Poster poster, Scheduler diskScheduler) {
        super(chatContext);
        this.apiService = apiService;
        this.dao = dao;
        this.poster = poster;
        this.diskScheduler = diskScheduler;
    }

    @Override
    public Observable<User> getUserFromDisk(final String id) {
        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> e) throws Exception {
                        e.onNext(dao.getUser(id));
                        e.onComplete();
                    }
                })
                .subscribeOn(diskScheduler)
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        poster.postUserChanged(user);
                    }
                });
    }

    @Override
    public Observable<User> getUserFromServer(String id) {
        return apiService.getUserById(id)
                .map(new Function<UserProtos.UserResponse, User>() {
                    @Override
                    public User apply(UserProtos.UserResponse userResponse) throws Exception {
                        ApiUtils.requireProtoResponseSuccessful(userResponse);
                        if (userResponse.getData() == null) {
                            throw new NullPointerException("UserBean == null");
                        }
                        User user = new User();
                        user.setId(userResponse.getData().getId());
                        user.setName(userResponse.getData().getName());
                        user.setNick(userResponse.getData().getNick());
                        user.setPortraitUrl(userResponse.getData().getPortraitUrl());
                        user.setGender(userResponse.getData().getGender());
                        saveUserInternal(user);
                        return user;
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "getUserFromServer", throwable);
                    }
                });
    }

    @Override
    public User getUser(String id) {
        final UserProvider userProvider = this.userProvider;
        return userProvider != null ? userProvider.getUser(id) : null;
    }

    private void saveUserInternal(User user) {
        dao.insertOrUpdateMessage(user);
        poster.postUserChanged(user);
    }

    @Override
    public void setUserProvider(UserProvider userProvider) {
        this.userProvider = userProvider;
    }
}
