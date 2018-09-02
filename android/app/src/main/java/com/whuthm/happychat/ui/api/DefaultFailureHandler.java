package com.whuthm.happychat.ui.api;

import android.content.Context;
import android.widget.Toast;

import com.whuthm.happychat.R;
import com.whuthm.happychat.data.api.ApiResponseObserver;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

 class DefaultFailureHandler implements ApiResponseObserver.FailureHandler {

    private final Context context;

    DefaultFailureHandler(Context context) {
        this.context = context;
    }

     @Override
     public void handle(Throwable e) {
         if (e instanceof SocketTimeoutException) {
             Toast.makeText(context, R.string.toast_net_error_timeout, Toast.LENGTH_SHORT)
                     .show();
         } else if (e instanceof ConnectException) {
             Toast.makeText(context, R.string.toast_net_error_http, Toast.LENGTH_SHORT)
                     .show();
         } else if (e instanceof HttpException) {
             Toast.makeText(context, R.string.toast_net_error_http, Toast.LENGTH_SHORT)
                     .show();
         } else {
             Toast.makeText(context, R.string.toast_net_error_default, Toast.LENGTH_SHORT)
                     .show();
         }
     }

}
