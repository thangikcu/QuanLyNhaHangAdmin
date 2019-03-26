package com.coffeehouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coffeehouse.util.AvoidDoubleClick;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    public List<T> listItem;

    protected OnItemClickListener onItemClickListener;

    public BaseRecyclerViewAdapter(List<T> listItem) {
        this.listItem = listItem;
    }

    public BaseRecyclerViewAdapter(List<T> listItem, OnItemClickListener onItemClickListener) {
        this(listItem);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return listItem != null ? listItem.size() : 0;
    }

    public T getLastItem() {
        return listItem.get(listItem.size() - 1);
    }

    public T getFirstItem() {
        return listItem.get(0);
    }

    public T getItemAt(int position) {
        if (position >= 0 && position < listItem.size()) {
            return listItem.get(position);
        }
        return null;
    }

    public void addItem(T item) {
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
        addItem(item, listItem.size());
    }

    public void addItem(T item, int position) {
        if (listItem == null) {
            listItem = new ArrayList<>();
        }

        listItem.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(List<T> listItem) {
        if (this.listItem == null) {
            this.listItem = new ArrayList<>();
        }
        addItem(listItem, this.listItem.size());
    }

    public void addItem(List<T> listItem, int position) {
        if (listItem.size() > 0) {
            if (this.listItem == null) {
                this.listItem = new ArrayList<>();
            }

            this.listItem.addAll(position, listItem);
            notifyItemRangeInserted(position, listItem.size());
        }
    }

    public void removeItem(T item) {
        int index = listItem.indexOf(item);
        if (index != -1) {
            removeItem(index);
        }
    }

    public void removeItem(int position) {
        listItem.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        listItem.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder vh, int position) {
        onBindView((VH) vh, getItemAt(position));
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        VH itemViewHolder = onCreateItemView(viewGroup, viewType);

        if (onItemClickListener != null) {
            itemViewHolder.itemView.setOnClickListener(new AvoidDoubleClick() {
                @Override
                protected void onClicked(View view) {
                    onItemClickListener.onItemClick(itemViewHolder.itemView, itemViewHolder.getAdapterPosition());
                }
            });
        }
        return itemViewHolder;
    }

    protected View inflateView(ViewGroup parent, int resLayout) {
        return LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
    }

    public List<T> getListItem() {
        return listItem;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected abstract VH onCreateItemView(ViewGroup parent, int viewType);

    protected abstract void onBindView(VH viewHolder, T item);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


