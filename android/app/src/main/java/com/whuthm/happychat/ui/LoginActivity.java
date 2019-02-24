package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barran.lib.app.BaseActivity;
import com.barran.lib.utils.log.Logs;
import com.barran.lib.view.text.LimitEditText;
import com.barran.lib.view.text.LimitTextWatcher;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.data.api.ApiResponseObserver;
import com.whuthm.happychat.ui.api.FailureHandlers;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 登录界面
 * 
 * Created by huangming on 18/07/2018.
 */

public class LoginActivity extends BaseActivity {
    
    private LimitEditText mETAccount;
    private LimitEditText mETPassword;
    private TextView mTVSubmit;
    private ImageView mIvPasswordHidden;
    
    private boolean isPasswordHidden = true;

    private AuthenticationService authenticationService;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authenticationService = ApplicationServiceContext.of(this).getService(AuthenticationService.class);
        
        mETAccount = findViewById(R.id.activity_login_edit_account);
        mETAccount.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));
        mETPassword = findViewById(R.id.activity_login_edit_password);
        mETPassword.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));
        
        ClickListener listener = new ClickListener();
        mIvPasswordHidden = findViewById(R.id.activity_login_image_hide_password);
        mIvPasswordHidden.setOnClickListener(listener);
        
        mTVSubmit = findViewById(R.id.fragment_pwd_login_tv_submit);
        mTVSubmit.setOnClickListener(listener);

        mETAccount.setText(authenticationService.getAuthenticationUser().getUsername());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_register) {
            startActivity(new Intent(this, RegisterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkSubmitButton() {
        if (!TextUtils.isEmpty(mETAccount.getText().toString())
                && !TextUtils.isEmpty(mETPassword.getText().toString())) {
            mTVSubmit.setEnabled(true);
        }
        else {
            mTVSubmit.setEnabled(false);
        }
    }
    
    private void reqLogin() {
        final String username = mETAccount.getText().toString();

        AuthenticationProtos.LoginRequest.Builder builder = AuthenticationProtos.LoginRequest
                .newBuilder();
        builder.setUsername(username)
                .setPassword(mETPassword.getText().toString())
                .setClientResource(ClientProtos.ClientResource.phone)
                .setPublicKey("");
        authenticationService.login(builder.build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiResponseObserver<AuthenticationUser>(FailureHandlers.getDefault(this)) {

                    @Override
                    public void onSuccess(AuthenticationUser response) {
                        toMainActivity();
                    }

                    @Override
                    public boolean onFailure(Throwable error) {
                        return false;
                    }
                });
        
    }
    
    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                
                case R.id.activity_login_image_hide_password:
                    isPasswordHidden = !isPasswordHidden;
                    if (isPasswordHidden) {
                        mIvPasswordHidden.setImageResource(R.drawable.password_hidden);
                        mETPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
                                | InputType.TYPE_CLASS_TEXT);
                    }
                    else {
                        mIvPasswordHidden.setImageResource(R.drawable.password_visible);
                        mETPassword.setInputType(
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    break;
                
                case R.id.fragment_pwd_login_tv_submit:
                    reqLogin();
                    break;
            }
        }
    }
}
