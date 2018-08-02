package com.barran.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 封装item点击
 *
 * Created by tanwei on 2017/10/21.
 */

public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    
    protected RecyclerViewItemClickListener itemClickListener;
    
    private View.OnClickListener mOnClickListener;
    
    public void setItemClickListener(RecyclerViewItemClickListener l) {
        if (l != null) {
            itemClickListener = l;
            
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick((int) view.getTag());
                }
            };
        }
    }
    
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        final VH viewHolder = createHolder(parent, viewType);
        if (itemClickListener != null) {
            viewHolder.itemView.setOnClickListener(mOnClickListener);
        }
        return viewHolder;
    }
    
    protected abstract VH createHolder(ViewGroup parent, int viewType);
    
    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        
        holder.itemView.setTag(position);
    }
    
    public interface RecyclerViewItemClickListener {
        void onItemClick(int position);
    }
}
