package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.IMClient;
import com.whuthm.happychat.imlib.event.EventBusUtils;

public class IMContextFragment extends UiFragment {

    protected IMContext imContext;
    protected ApplicationServiceContext applicationServiceContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        applicationServiceContext = ApplicationServiceContext.of(getContext());
        imContext = IMClient.getInstance().getIMContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBusUtils.safeRegister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusUtils.safeUnregister(this);
    }
}
