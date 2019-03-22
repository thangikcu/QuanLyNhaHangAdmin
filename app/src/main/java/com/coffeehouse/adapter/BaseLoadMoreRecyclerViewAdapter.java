package com.coffeehouse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.coffeehouse.R;

import java.util.List;

public abstract class BaseLoadMoreRecyclerViewAdapter<T, VH extends BaseRecyclerViewAdapter.BaseViewHolder> extends BaseRecyclerViewAdapter<T, VH> {
    protected final int LOADING_TYPE = 1;

    protected OnLoadMoreListener onLoadMoreListener;
    protected OnBottomReachedListener onBottomReachedListener;
    protected boolean isRemaingData = true;
    protected int numberItemPerPage;

    protected boolean isLoading;
    protected int currentPageIndex = 0;

    private boolean isInitLoadMoreListener;

    public BaseLoadMoreRecyclerViewAdapter(List<T> listItem, OnLoadMoreListener onLoadMore, int numberItemPerPage) {
        super(listItem);
        this.onLoadMoreListener = onLoadMore;
        this.numberItemPerPage = numberItemPerPage;
    }

    public BaseLoadMoreRecyclerViewAdapter(List<T> listItem, OnLoadMoreListener onLoadMore, int numberItemPerPage, OnItemClickListener onItemClickListener) {
        this(listItem, onLoadMore, numberItemPerPage);
        this.onItemClickListener = onItemClickListener;
    }

    public T getLastItem() {
        if (isLoading) {
            return listItem.get(listItem.size() - 2);
        } else {
            return super.getLastItem();
        }
    }

    public void clear() {
        isRemaingData = true;
        currentPageIndex = 0;
        super.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemAt(position) == null) {
            return LOADING_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        initLoadMoreListener(viewGroup);

        if (viewType == LOADING_TYPE) {
            return OnCreateLoadingView(viewGroup);
        } else {
            return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder vh, int position) {
        if (!(vh instanceof LoadingViewHoler)) {
            super.onBindViewHolder(vh, position);
        }
    }

    private void initLoadMoreListener(ViewGroup parent) {
        if ((onLoadMoreListener != null || onBottomReachedListener != null) && !isInitLoadMoreListener) {
            RecyclerView recyclerView = (RecyclerView) parent;

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                isInitLoadMoreListener = true;

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (onBottomReachedListener != null && dy > 0 && !recyclerView.canScrollVertically(1)) {
                            onBottomReachedListener.onBottomReached();
                        }

                        if (onLoadMoreListener != null && !isLoading && isRemaingData) {
                            if (getItemCount() >= numberItemPerPage &&
                                    (linearLayoutManager.getReverseLayout()
                                            ? (dy < 0 && !recyclerView.canScrollVertically(-1))
                                            : (dy > 0 && !recyclerView.canScrollVertically(1)))) {
                                setLoading(true);
                                recyclerView.scrollToPosition(getItemCount() - 1);
                                recyclerView.postDelayed(() -> onLoadMoreListener.onLoadMore(++currentPageIndex), 1000);

                            }
                        }
                    }
                });
            }
        }
    }

    protected LoadingViewHoler OnCreateLoadingView(ViewGroup parent) {
        return new LoadingViewHoler(inflateView(parent, R.layout.item_loading));
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public boolean isRemaingData() {
        return isRemaingData;
    }

    public void setRemaingData(boolean remaingData) {
        isRemaingData = remaingData;
    }

    public int getNumberItemPerPage() {
        return numberItemPerPage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        if (this.isLoading != isLoading) {
            this.isLoading = isLoading;
            if (isLoading) {
                addItem((T) null);
            } else {
                int index = listItem.size() - 1;
                if (index >= 0) {
                    removeItem(index);
                }
            }
        }
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int pageIndex);
    }

    public interface OnBottomReachedListener {
        void onBottomReached();
    }

    private static class LoadingViewHoler extends BaseViewHolder {
        LoadingViewHoler(View itemView) {
            super(itemView);
        }
    }
}


