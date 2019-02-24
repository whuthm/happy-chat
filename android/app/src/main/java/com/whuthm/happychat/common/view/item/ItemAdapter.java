package com.whuthm.happychat.common.view.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangming on 2018/12/21.
 */

public class ItemAdapter<ITEM> extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder<ITEM>> {

    private final Context context;
    private List<ITEM> items;
    private ItemViewProvider<ITEM> itemViewProvider;
    private Comparator<ITEM> sortComparator;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public ItemAdapter(Context context, List<ITEM> items) {
        this.context = context;
        this.items = items;
    }

    public void setSortComparator(Comparator<ITEM> sortComparator) {
        this.sortComparator = sortComparator;
    }

    public Context getContext() {
        return context;
    }

    public void setItems(List<ITEM> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItemViewProvider(ItemViewProvider<ITEM> itemViewProvider) {
        this.itemViewProvider = itemViewProvider;
    }

    public void removeItem(int index) {
        if (index >= 0 && index < getItemCount()) {
            this.items.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void removeItem(ITEM item) {
        removeItem(indexOf(item));
    }

    public void addItem(ITEM item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (item != null) {
            final int oldItemCount = getItemCount();
            this.items.add(item);
            if (shouldSortItems()) {
                notifyDataSetChanged();
                return;
            }
            notifyItemInserted(oldItemCount);
        }
    }

    public void addItem(int index, ITEM item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (item != null && this.items != null) {
            final int oldItemCount = getItemCount();
            if (index < 0 || index > oldItemCount) {
                index = oldItemCount;
            }
            this.items.add(index, item);
            if (shouldSortItems()) {
                notifyDataSetChanged();
                return;
            }
            notifyItemInserted(index);
        }
    }

    public void changeItem(ITEM item) {
        int index = indexOf(item);
        if (index >=0 && index < getItemCount()) {
            this.items.set(index, item);
            if (shouldSortItems()) {
                notifyDataSetChanged();
                return;
            }
            notifyItemChanged(index);
        }
    }

    public void changeItems(List<ITEM> items) {
        final int itemCount = getItemCount();
        if (items != null && items.size() > 0 && itemCount > 0) {
            int positionStart = -1;
            int positionEnd = -1;
            for (ITEM item : items) {
                int index = indexOf(item);
                if (index >= 0 && index < itemCount) {
                    this.items.set(index, item);
                    if (index < positionStart || positionStart < 0) {
                        positionStart = index;
                    }
                    if (index > positionEnd) {
                        positionEnd = index;
                    }
                }
            }
            if (shouldSortItems()) {
                notifyDataSetChanged();
                return;
            }
            if (positionEnd >= positionStart && positionStart >= 0) {
                notifyItemRangeChanged(positionStart, positionEnd - positionStart + 1);
            }
        }
    }

    public void clear() {
        if (getItemCount() > 0) {
            this.items.clear();
            notifyDataSetChanged();
        }
    }

    private void sortItems() {
        if (sortComparator != null && getItemCount() > 1) {
            Collections.sort(items, sortComparator);
        }
    }

    private boolean shouldSortItems() {
        return sortComparator != null;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public ITEM getItem(int index) {
        return index >= 0 && index < getItemCount() ? items.get(index) : null;
    }

    public int indexOf(ITEM item) {
        return item != null && items != null ? items.indexOf(item) : -1;
    }

    @Override
    public ItemViewHolder<ITEM> onCreateViewHolder(ViewGroup parent, int viewType) {
        return itemViewProvider != null ? itemViewProvider.getItemViewHolder(this, parent, viewType) : null;
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewProvider != null ? itemViewProvider.getItemViewType(this, position) : super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder<ITEM> holder, int position) {
        holder.bindView(getItem(position), position);
    }

    public static class ItemViewHolder<ITEM> extends RecyclerView.ViewHolder {

        protected final Context context;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
        }

        protected void bindView(ITEM item, int position) {
        }

        protected final <T extends View> T findViewById(int viewId) {
            return itemView.findViewById(viewId);
        }

    }

    public interface ItemViewProvider<ITEM> {

        ItemViewHolder<ITEM> getItemViewHolder(ItemAdapter<ITEM> adapter, ViewGroup parent, int viewType);

        /**
         * 获取ItemView type，必须大于0, 从1开始有序排列
         */
        int getItemViewType(ItemAdapter<ITEM> adapter, int position);
    }

}
