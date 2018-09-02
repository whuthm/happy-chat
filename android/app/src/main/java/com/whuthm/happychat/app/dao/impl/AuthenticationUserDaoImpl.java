package com.whuthm.happychat.app.dao.impl;

import android.content.Context;

import com.barran.lib.data.SPWrapper;
import com.whuthm.happychat.app.dao.IAuthenticationUserDao;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.utils.SharedPreferencesUtils;

class AuthenticationUserDaoImpl implements IAuthenticationUserDao {

    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_AVATAR = "user_avatar";
    public static final String KEY_USER_NICK = "user_nick";
    public static final String KEY_USER_TOKEN = "user_token";

    private final SPWrapper spWrapper;

    AuthenticationUserDaoImpl(Context context) {
        spWrapper = SharedPreferencesUtils.getGlobalSharedPreferences(context);
    }

    @Override
    public AuthenticationUser getAuthenticationUser() {
        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setUserId(spWrapper.getString(KEY_USER_ID, ""));
        authenticationUser.setUserToken(spWrapper.getString(KEY_USER_TOKEN, ""));
        authenticationUser.setUsername(spWrapper.getString(KEY_USER_NAME, ""));
        authenticationUser.setUserAvatar(spWrapper.getString(KEY_USER_AVATAR, ""));
        authenticationUser.setUserNick(spWrapper.getString(KEY_USER_NICK, ""));
        return authenticationUser;
    }

    @Override
    public void saveAuthenticationUser(AuthenticationUser authenticationUser) {
        spWrapper.put(KEY_USER_ID, authenticationUser.getUserId());
        spWrapper.put(KEY_USER_TOKEN, authenticationUser.getUserToken());
        spWrapper.put(KEY_USER_NAME, authenticationUser.getUsername());
        spWrapper.put(KEY_USER_AVATAR, authenticationUser.getUserAvatar());
        spWrapper.put(KEY_USER_NICK, authenticationUser.getUserNick());
    }
}
