package com.github.zchu.common.help;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵成柱 on 2015/12/14.
 * 帮你简化PagerAdapter的编写
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {


    protected List<T> data;
    private List<String> strPageTitles;
    private List<Integer> layoutResIds;
    private int layoutResId;
    private boolean isSingleLayoutRes;
    private boolean isFromResId;


    public BasePagerAdapter(List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
    }

    public BasePagerAdapter(List<T> data, @LayoutRes int layoutResId) {
        this.data = data == null ? new ArrayList<T>() : data;
        isFromResId = true;
        isSingleLayoutRes = true;
        this.layoutResId = layoutResId;

    }

    public BasePagerAdapter(List<T> data, List<Integer> layoutResIds) {
        this(data, layoutResIds, null);
    }

    public BasePagerAdapter(List<T> data, List<Integer> layoutResIds, List<String> strPageTitles) {
        isFromResId = true;
        this.data = data == null ? new ArrayList<T>() : data;
        this.layoutResIds = layoutResIds;
        this.strPageTitles = strPageTitles;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if (isFromResId) {
            int layoutResId;
            if (isSingleLayoutRes) {
                layoutResId = this.layoutResId;
            } else {
                layoutResId = layoutResIds.get(position % layoutResIds.size());
            }
            view = LayoutInflater.from(container.getContext())
                    .inflate(layoutResId, container, false);
        } else {
            view = getView(container, position);
        }


        convert(view, getItem(position), position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public String getPageTitle(int position) {
        return strPageTitles == null ? null : strPageTitles.get(position);
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    public T getItem(int position) {
        if (data == null || position >= data.size()) return null;
        return data.get(position);
    }


    protected abstract void convert(View view, T item, int position);


    public void addAll(List<T> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setNewData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p>
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    protected View getView(ViewGroup container, int position) {
        throw new RuntimeException("如果没有设置layoutResId，则必须重写getView()");
    }


}
