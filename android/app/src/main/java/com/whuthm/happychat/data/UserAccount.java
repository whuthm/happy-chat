package com.whuthm.happychat.data;

import android.content.Context;

import com.barran.lib.data.SPWrapper;

/**
 * 用户信息管理
 * 
 * Created by tanwei on 2018/7/31.
 */

public class UserAccount {
    
    private static UserAccount sInstance;
    
    private Context context;
    
    private SPWrapper spWrapper;
    
    private String userName;
    
    private String userId;
    
    private String userNick;
    
    private String token;
    
    private String key;
    
    private UserAccount(Context context) {
        this.context = context;
        spWrapper = new SPWrapper(context, "user");
    }
    
    public static void init(Context context) {
        sInstance = new UserAccount(context);
        sInstance.loadData();
    }
    
    public void loadData() {
        userName = spWrapper.getString(Constants.KEY_USER_NAME);
        userId = spWrapper.getString(Constants.KEY_USER_ID);
        userNick = spWrapper.getString(Constants.KEY_USER_NICK);
        token = spWrapper.getString(Constants.KEY_USER_TOKEN);
        key = spWrapper.getString(Constants.KEY_KEYSTORE);
    }
    
    public static void saveUser(AuthenticationProtos.LoginResponse response) {
        setToken(response.getToken());
        setUserId(response.getUserId());
        setKey(response.getKeystore());
    }
    
    public static String getUserName() {
        return sInstance.userName;
    }
    
    public static void setUserName(String userName) {
        sInstance.userName = userName;
        sInstance.spWrapper.put(Constants.KEY_USER_NAME, userName);
    }
    
    public static String getToken() {
        return sInstance.token;
    }
    
    public static void setToken(String token) {
        sInstance.token = token;
        sInstance.spWrapper.put(Constants.KEY_USER_TOKEN, token);
    }
    
    public static String getKey() {
        return sInstance.key;
    }
    
    public static void setKey(String key) {
        sInstance.key = key;
        sInstance.spWrapper.put(Constants.KEY_KEYSTORE, key);
    }
    
    public static String getUserId() {
        return sInstance.userId;
    }
    
    public static void setUserId(String userId) {
        sInstance.userId = userId;
        sInstance.spWrapper.put(Constants.KEY_USER_ID, userId);
    }
    
    public static String getUserNick() {
        return sInstance.userNick;
    }
    
    public static void setUserNick(String userNick) {
        sInstance.userNick = userNick;
        sInstance.spWrapper.put(Constants.KEY_USER_NICK, userNick);
    }
}
