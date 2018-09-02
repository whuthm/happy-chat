package com.whuthm.happychat.utils;

import android.content.Context;

import com.barran.lib.data.SPWrapper;

public class SharedPreferencesUtils {

    private SharedPreferencesUtils() {
    }

    public static SPWrapper getGlobalSharedPreferences(Context context) {
        return new SPWrapper(context, "happy-chat-global");
    }

    public static SPWrapper getUserSharedPreferences(Context context, String userId) {
        return new SPWrapper(context, "happy-chat-user-" + userId);
    }

}
