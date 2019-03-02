package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barran.lib.ui.recycler.VerticalDividerDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whuthm.happychat.R;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.ItemsRecyclerView;
import com.whuthm.happychat.data.UserProtos;
import com.whuthm.happychat.data.api.ApiUtils;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.imlib.model.ConversationType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class MainContactsFragment extends IMContextFragment {

    private ItemsRecyclerView recyclerView;
    private ItemAdapter<UserProtos.UserBean> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalDividerDecoration(getActivity()));
        adapter = new ItemAdapter<>(getActivity());
        adapter.setItemViewProvider(new ItemAdapter.ItemViewProvider<UserProtos.UserBean>() {
            @Override
            public ItemAdapter.ItemViewHolder<UserProtos.UserBean> getItemViewHolder(ItemAdapter<UserProtos.UserBean> adapter, ViewGroup parent, int viewType) {
                return new UserItemViewHolder(LayoutInflater.from(adapter.getContext()).inflate(R.layout.item_user, parent, false));
            }

            @Override
            public int getItemViewType(ItemAdapter<UserProtos.UserBean> adapter, int position) {
                return 0;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new ItemsRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View v, int position) {
                UserProtos.UserBean userBean = adapter.getItem(position);
                if (userBean != null) {
                    ConversationActivity.startConversation(getContext(), userBean.getId(), ConversationType.PRIVATE, userBean.getName());
                }
            }
        });

        RetrofitClient.api()
                .getUsers()
                .doOnNext(new Consumer<UserProtos.UsersResponse>() {
                    @Override
                    public void accept(UserProtos.UsersResponse usersResponse) throws Exception {
                        ApiUtils.requireProtoResponseSuccessful(usersResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addDisposable(new DisposableObserver<UserProtos.UsersResponse>() {
                    @Override
                    public void onNext(UserProtos.UsersResponse value) {
                        adapter.setItems(value.getDataList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getTag(), "getUsers", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public static class UserItemViewHolder extends ItemAdapter.ItemViewHolder<UserProtos.UserBean> {

        private TextView nameView;
        private ImageView portraitView;

        public UserItemViewHolder(View itemView) {
            super(itemView);
            nameView = findViewById(R.id.tv_name);
            portraitView = findViewById(R.id.iv_portrait);
        }

        @Override
        protected void bindView(UserProtos.UserBean userBean, int position) {
            super.bindView(userBean, position);
            nameView.setText(userBean.getName());
            Glide.with(context)
                    .load(userBean.getPortraitUrl())
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.dog1)
                            .error(R.drawable.dog1))
                    .into(portraitView);
        }
    }
}
