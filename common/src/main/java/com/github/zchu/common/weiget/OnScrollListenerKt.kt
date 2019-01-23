package com.github.zchu.common.weiget

import android.widget.AbsListView

class AbsListViewOnScrollListenerBridge : AbsListView.OnScrollListener {
    private var _onScroll: ((view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) -> Unit)? =
        null

    private var _onScrollStateChanged: ((view: AbsListView?, scrollState: Int) -> Unit)? =
        null

    fun _onScroll(t: ((view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) -> Unit)) {
        _onScroll = t
    }

    fun _onScrollStateChanged(t: ((view: AbsListView?, scrollState: Int) -> Unit)) {
        _onScrollStateChanged = t
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        _onScroll?.invoke(view, firstVisibleItem, visibleItemCount, totalItemCount)
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        _onScrollStateChanged?.invoke(view, scrollState)
    }
}

//also available for gridview
inline fun AbsListView._setOnScrollListener(func: (AbsListViewOnScrollListenerBridge.() -> Unit)) =
    setOnScrollListener(AbsListViewOnScrollListenerBridge().apply(func))
