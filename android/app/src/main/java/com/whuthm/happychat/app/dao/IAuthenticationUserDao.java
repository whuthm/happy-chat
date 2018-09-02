package com.whuthm.happychat.app.dao;

import com.whuthm.happychat.app.model.AuthenticationUser;

public interface IAuthenticationUserDao {

     AuthenticationUser getAuthenticationUser();

     void saveAuthenticationUser(AuthenticationUser authenticationUser);
}
