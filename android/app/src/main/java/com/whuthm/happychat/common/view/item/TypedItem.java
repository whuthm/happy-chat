package com.whuthm.happychat.common.view.item;

import com.whuthm.happychat.common.util.ObjectUtils;

/**
 * Created by huangming on 2018/12/22.
 */

public final class TypedItem<ITEM> {

    private final int type;
    private final ITEM item;

    public TypedItem(int type) {
        this(type, null);
    }

    public TypedItem(int type, ITEM item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public ITEM getItem() {
        return item;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TypedItem<?> && getClass() == obj.getClass()) {
            TypedItem<?> o = (TypedItem<?>) obj;
            return ObjectUtils.equals(o.getType(), getType()) && ObjectUtils.equals(o.getItem(), getItem());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(type, item);
    }

}
