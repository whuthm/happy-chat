package com.whuthm.happychat.data.api;

import android.content.Context;

import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.util.StringUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求拦截器，可设置统一的参数
 * 
 * Created by tanwei on 2018/7/20.
 */

public class HttpInterceptor implements Interceptor {

    private final Context context;
    private final Map<String, String> headers;
    
    public HttpInterceptor(Context context, Map<String, String> headers) {
        this.context = context;
        this.headers = headers;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && !headers.isEmpty()) {
            
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        AuthenticationService authenticationService = ApplicationServiceContext.of(context).getService(AuthenticationService.class);
        AuthenticationUser authenticationUser = authenticationService.getAuthenticationUser();
        if (authenticationUser != null) {
            if (!StringUtils.isEmpty(authenticationUser.getUserToken())) {
                builder.addHeader("token", authenticationUser.getUserToken());
            }

            if (!StringUtils.isEmpty(authenticationUser.getUserId())) {
                builder.addHeader("user_id", authenticationUser.getUserId());
            }

            builder.addHeader("client_resource", ClientProtos.ClientResource.phone.name());
        }
        return chain.proceed(builder.build());
        
    }
}
