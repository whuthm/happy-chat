package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.barran.lib.app.BaseActivity;
import com.barran.lib.view.text.LimitEditText;
import com.barran.lib.view.text.LimitTextWatcher;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.api.ApiResponseObserver;
import com.whuthm.happychat.ui.api.FailureHandlers;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private LimitEditText mETAccount;
    private LimitEditText mETPassword;
    private TextView mTVSubmit;
    private ImageView mIvPasswordHidden;

    private boolean isPasswordHidden = true;

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authenticationService = ApplicationServiceContext.of(this).getService(AuthenticationService.class);

        mETAccount = findViewById(R.id.activity_register_edit_account);
        mETAccount.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));
        mETPassword = findViewById(R.id.activity_register_edit_password);
        mETPassword.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));

        mIvPasswordHidden = findViewById(R.id.activity_register_image_hide_password);
        mIvPasswordHidden.setOnClickListener(this);

        mTVSubmit = findViewById(R.id.fragment_pwd_register_tv_submit);
        mTVSubmit.setOnClickListener(this);

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

    private void reqRegister() {
        final String username = mETAccount.getText().toString();

        AuthenticationProtos.RegisterRequest.Builder builder = AuthenticationProtos.RegisterRequest
                .newBuilder();
        builder.setUsername(username)
                .setPassword(mETPassword.getText().toString());
        authenticationService.register(builder.build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiResponseObserver<AuthenticationUser>(FailureHandlers.getDefault(this)) {

                    @Override
                    public void onSuccess(AuthenticationUser response) {
                        finish();
                    }

                    @Override
                    public boolean onFailure(Throwable error) {
                        return false;
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.activity_register_image_hide_password:
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

            case R.id.fragment_pwd_register_tv_submit:
                reqRegister();
                break;
        }
    }

}
