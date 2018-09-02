package com.whuthm.happychat.ui.api;

import android.content.Context;

import com.whuthm.happychat.data.api.ApiResponseObserver;

public final class FailureHandlers {

    public static ApiResponseObserver.FailureHandler getDefault(Context context) {
        return new DefaultFailureHandler(context);
    }

}
