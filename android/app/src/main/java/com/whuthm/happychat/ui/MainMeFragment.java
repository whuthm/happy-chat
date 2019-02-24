package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whuthm.happychat.R;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.imlib.UserService;
import com.whuthm.happychat.imlib.model.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * 个人信息界面
 * <p>
 * Created by huangming on 18/07/2018.
 */

public class MainMeFragment extends IMContextFragment {

    private EditText editNick;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editNick = view.findViewById(R.id.frag_me_input_nick);
        editNick.setText(applicationServiceContext.getService(AuthenticationService.class).getAuthenticationUser().getUserNick());

        view.findViewById(R.id.frag_me_btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userNick = editNick.getText().toString().trim();
                        if (!TextUtils.isEmpty(userNick)) {
                            //AuthenticationUser.setUserNick(userNick);
                        }
                    }
                });

        imContext.getService(UserAppService.class)
                .getCurrentUserFromServer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addDisposable(new DisposableObserver<User>() {

                    @Override
                    public void onNext(User value) {
                        editNick.setText(value.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getTag(), "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
