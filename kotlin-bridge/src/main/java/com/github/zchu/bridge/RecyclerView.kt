package com.github.zchu.bridge

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewOnScrollListenerBridge : RecyclerView.OnScrollListener() {

    private var _onScrolled: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Unit)? = null
    private var _onScrollStateChanged: ((recyclerView: RecyclerView, newState: Int) -> Unit)? = null


    fun _onScrolled(func: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Unit)) {
        _onScrolled = func
    }

    fun _onScrollStateChanged(func: ((recyclerView: RecyclerView, newState: Int) -> Unit)) {
        _onScrollStateChanged = func
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        _onScrolled?.invoke(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        _onScrollStateChanged?.invoke(recyclerView, newState)
    }
}


class OnItemTouchListenerBridge : RecyclerView.OnItemTouchListener {
    private var _onTouchEvent: ((rv: RecyclerView, e: MotionEvent) -> Unit)? = null
    private var _onInterceptTouchEvent: ((rv: RecyclerView, e: MotionEvent) -> Boolean)? = null
    private var _onRequestDisallowInterceptTouchEvent: ((disallowIntercept: Boolean) -> Unit)? = null

    fun _onTouchEvent(func: ((rv: RecyclerView, e: MotionEvent) -> Unit)) {
        _onTouchEvent = func
    }

    fun _onInterceptTouchEvent(func: ((rv: RecyclerView, e: MotionEvent) -> Boolean)) {
        _onInterceptTouchEvent = func
    }

    fun _onRequestDisallowInterceptTouchEvent(func: ((disallowIntercept: Boolean) -> Unit)) {
        _onRequestDisallowInterceptTouchEvent = func
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        _onTouchEvent?.invoke(rv, e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return _onInterceptTouchEvent?.invoke(rv, e) ?: false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        _onRequestDisallowInterceptTouchEvent?.invoke(disallowIntercept)
    }
}

inline fun RecyclerView._addOnScrollListener(func: (RecyclerViewOnScrollListenerBridge.() -> Unit)) =
    addOnScrollListener(RecyclerViewOnScrollListenerBridge().apply(func))

inline fun RecyclerView._addOnItemTouchListener(func: (OnItemTouchListenerBridge.() -> Unit)) =
    addOnItemTouchListener(OnItemTouchListenerBridge().apply(func))
