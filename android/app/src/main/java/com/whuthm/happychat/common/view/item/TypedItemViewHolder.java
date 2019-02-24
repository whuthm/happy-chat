package com.whuthm.happychat.common.view.item;

import android.view.View;

/**
 * Created by huangming on 2018/12/22.
 */

public class TypedItemViewHolder<ITEM> extends ItemAdapter.ItemViewHolder<TypedItem<?>> {
    public TypedItemViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bindView(TypedItem<?> typedItem, int position) {
        super.bindView(typedItem, position);
        if (typedItem.getItem() != null) {
            onBindView(typedItem, (ITEM) typedItem.getItem(), position);
        }
    }

    protected void onBindView(TypedItem<?> typedItem, ITEM item, int position) {
    }

    public static <ITEM> TypedItemViewHolder<ITEM> wrap(ItemAdapter.ItemViewHolder<ITEM> wrapped) {
        return new Wrapper<>(wrapped);
    }

    private static class Wrapper<ITEM> extends TypedItemViewHolder<ITEM> {

        final ItemAdapter.ItemViewHolder<ITEM> wrapped;

        public Wrapper(ItemAdapter.ItemViewHolder<ITEM> wrapped) {
            super(wrapped.itemView);
            this.wrapped = wrapped;
        }

        @Override
        protected void onBindView(TypedItem<?> typedItem, ITEM item, int position) {
            super.onBindView(typedItem, item, position);
            this.wrapped.bindView(item, position);
        }
    }
}
