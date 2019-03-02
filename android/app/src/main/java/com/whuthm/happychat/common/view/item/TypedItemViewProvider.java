package com.whuthm.happychat.common.view.item;

import android.view.ViewGroup;


/**
 * Created by huangming on 2018/12/22.
 *
 * 有类型的ItemView提供者
 */

public abstract class TypedItemViewProvider implements ItemAdapter.ItemViewProvider<TypedItem<?>> {

    @Override
    public final int getItemViewType(ItemAdapter<TypedItem<?>> adapter, int position) {
        TypedItem<?> typedItem = adapter.getItem(position);
        return typedItem != null ? typedItem.getType() : 0;
    }

    protected abstract ItemAdapter.ItemViewHolder<?> onCreateItemViewHolder(ItemAdapter<TypedItem<?>> adapter, ViewGroup parent, int viewTyp);

    @Override
    public TypedItemViewHolder<?> getItemViewHolder(ItemAdapter<TypedItem<?>> adapter, ViewGroup parent, int viewType) {
        ItemAdapter.ItemViewHolder<?> holder = onCreateItemViewHolder(adapter, parent, viewType);
        onItemViewHolderCreated(adapter, holder);
        return TypedItemViewHolder.wrap(holder);
    }

    protected void onItemViewHolderCreated(ItemAdapter<TypedItem<?>> adapter, ItemAdapter.ItemViewHolder<?> holder) {
    }

}
