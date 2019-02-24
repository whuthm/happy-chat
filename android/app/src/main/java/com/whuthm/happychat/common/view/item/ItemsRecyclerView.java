package com.whuthm.happychat.common.view.item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangming on 2018/12/21.
 */

public class ItemsRecyclerView extends RecyclerView {

    private View emptyView;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public ItemsRecyclerView(Context context) {
        this(context, null);
    }

    public ItemsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                child.setOnClickListener(childClickListener);
                child.setOnLongClickListener(childLongClickListener);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                child.setOnClickListener(null);
                child.setOnLongClickListener(null);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    private OnClickListener childClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(ItemsRecyclerView.this, v, getChildAdapterPosition(v));
            }
        }
    };

    private OnLongClickListener childLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return itemLongClickListener != null && itemLongClickListener.onItemLongClick(ItemsRecyclerView.this, v, getChildAdapterPosition(v));
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mAdapterDataObserver);
        }
        super.setAdapter(adapter);
        checkIfEmpty();

        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
    }

    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            checkIfEmpty();
        }
    };

    private void checkIfEmpty() {
        if (emptyView != null) {
            final boolean empty = ((getAdapter() == null) || getAdapter().getItemCount() <= 0);
            emptyView.setVisibility(empty ? VISIBLE : GONE);
            setVisibility(empty ? GONE : VISIBLE);
        }
    }

    public  interface OnItemClickListener {

        void onItemClick(RecyclerView recyclerView, View v, int position);

    }

    public  interface OnItemLongClickListener {

        boolean onItemLongClick(RecyclerView recyclerView, View v, int position);

    }
}
