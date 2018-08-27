package com.whuthm.happychat.internal.context;

import android.app.Application;
import android.content.Context;

public class ApplicationServiceContext extends AbstractServiceContext {

    private final Application application;

    public ApplicationServiceContext(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }

    public static ApplicationServiceContext of(Context context) {
        if (context.getApplicationContext() instanceof Provider) {
            return ((Provider) context.getApplicationContext()).provideApplicationContext();
        } else {
            throw new RuntimeException("Application must be implements ApplicationServiceContext.Provider");
        }
    }

    public interface Provider {
        ApplicationServiceContext provideApplicationContext();
    }
}
