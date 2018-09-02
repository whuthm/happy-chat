package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.common.context.ApplicationServiceContext;

/**
 * 封禁
 * 
 * Created by huangming on 18/07/2018.
 */

public class SplashActivity extends BaseActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ApplicationServiceContext.of(getApplicationContext()).getService(AuthenticationService.class).getAuthenticationStatus() == AuthenticationStatus.LoggedIn) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else  {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
