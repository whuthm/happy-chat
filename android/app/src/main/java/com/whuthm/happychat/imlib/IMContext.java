package com.whuthm.happychat.imlib;

import android.content.Context;

import com.whuthm.happychat.common.context.ServiceContext;

public interface IMContext extends ServiceContext {

    Context getAndroidContext();

    IMOptions getOptions();

    ConnectionConfiguration getConfiguration();

    interface Initializer {
        void initialize(IMContext chatContext);
    }


}
