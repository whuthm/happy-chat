package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.barran.lib.app.BaseFragment;
import com.whuthm.happychat.R;
import com.whuthm.happychat.data.UserAccount;

/**
 * 个人信息界面
 * 
 * Created by huangming on 18/07/2018.
 */

public class MainMeFragment extends BaseFragment {
    
    private EditText editNick;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_me, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        editNick = view.findViewById(R.id.frag_me_input_nick);
        editNick.setText(UserAccount.getUserNick());
        
        view.findViewById(R.id.frag_me_btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userNick = editNick.getText().toString().trim();
                        if (!TextUtils.isEmpty(userNick)) {
                            UserAccount.setUserNick(userNick);
                        }
                    }
                });
    }
}
