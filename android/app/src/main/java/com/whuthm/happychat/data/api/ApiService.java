package com.whuthm.happychat.data.api;

import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.UserProtos;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 定义http请求api
 * 
 * Created by huangming on 18/07/2018.
 */

public interface ApiService {
    
    @POST("/v1/auth/login")
    Observable<AuthenticationProtos.LoginResponse> login(
            @Body AuthenticationProtos.LoginRequest request);

    @POST("/v1/auth/emailvcode")
    Observable<BaseProtos.BaseResponse> getEmailValidationCode(
            @Body BaseProtos.StringBean request);
    
    @POST("/v1/auth/register")
    Observable<BaseProtos.BaseResponse> register(
            @Body AuthenticationProtos.RegisterRequest request);
    
    @GET("/v1/users/{id}")
    Observable<UserProtos.UserResponse> getUserById(@Path("id") String userId);

    @GET("/v1/users")
    Observable<UserProtos.UsersResponse> getUsers();
    
    @POST("/v1/group/create")
    Observable<BaseProtos.BaseResponse> createGroup();
    
    @POST("/v1/group/quit")
    Observable<BaseProtos.BaseResponse> quitGroup(@Path("id") String id);
}
