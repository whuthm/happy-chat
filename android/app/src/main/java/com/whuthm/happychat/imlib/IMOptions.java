package com.whuthm.happychat.imlib;

import android.content.Context;

public class IMOptions {

    private final String imServer;
    private final int imPort;
    private final IMContext.Initializer initializer;
    private final Context androidContext;

    private IMOptions(Builder builder) {
        this.imServer = builder.imServer;
        this.imPort = builder.imPort;
        this.initializer = builder.initializer;
        this.androidContext = builder.androidContext;

    }

    Context getAndroidContext() {
        return androidContext;
    }

    int getImPort() {
        return imPort;
    }

    IMContext.Initializer getInitializer() {
        return initializer;
    }

    String getImServer() {
        return imServer;
    }

    public static class Builder {
        private final Context androidContext;
        private String imServer;
        private int imPort;
        private IMContext.Initializer initializer;

        public Builder(Context androidContext) {
            this.androidContext = androidContext;
        }

        public Builder setImPort(int imPort) {
            this.imPort = imPort;
            return this;
        }

        public Builder setImServer(String imServer) {
            this.imServer = imServer;
            return this;
        }

        public Builder setInitializer(IMContext.Initializer initializer) {
            this.initializer = initializer;
            return this;
        }

        public IMOptions build() {
            return new IMOptions(this);
        }

    }

}
