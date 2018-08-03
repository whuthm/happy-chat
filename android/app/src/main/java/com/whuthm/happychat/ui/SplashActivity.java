package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.R;

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
    }
}
