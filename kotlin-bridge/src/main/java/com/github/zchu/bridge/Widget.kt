package com.github.zchu.bridge

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import android.widget.SeekBar
import android.widget.TextView

fun View._setOnClickListener(block: ((v: View?) -> Unit)) {
    setOnClickListener(object : View.OnClickListener {
        var last = 0L
        override fun onClick(v: View?) {
            if (System.currentTimeMillis() - last > 500) {
                block(v)
                last = System.currentTimeMillis()
            }
        }
    })
}

fun View._setOnClickListener(intervalMill: Int, block: ((v: View?) -> Unit)) {
    setOnClickListener(object : View.OnClickListener {
        var last = 0L
        override fun onClick(v: View?) {
            if (System.currentTimeMillis() - last > intervalMill) {
                block(v)
                last = System.currentTimeMillis()
            }
        }
    })
}


class AttachStateChangeListenerBridge : View.OnAttachStateChangeListener {

    private var _onViewAttachedToWindow: ((view: View?) -> Unit)? = null
    private var _onViewDetachedFromWindow: ((view: View?) -> Unit)? = null

    fun _onViewAttachedToWindow(func: ((view: View?) -> Unit)) {
        _onViewAttachedToWindow = func
    }

    fun _onViewDetachedFromWindow(func: ((view: View?) -> Unit)) {
        _onViewDetachedFromWindow = func
    }

    override fun onViewAttachedToWindow(view: View?) {
        _onViewAttachedToWindow?.invoke(view)
    }

    override fun onViewDetachedFromWindow(view: View?) {
        _onViewDetachedFromWindow?.invoke(view)
    }
}

inline fun View._addOnAttachStateChangeListener(func: (AttachStateChangeListenerBridge.() -> Unit)) =
    addOnAttachStateChangeListener(AttachStateChangeListenerBridge().apply(func))


class SeekBarChangeListenerBridge : SeekBar.OnSeekBarChangeListener {

    private var _onProgressChanged: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null
    private var _onStartTrackingTouch: ((seekBar: SeekBar?) -> Unit)? = null
    private var _onStopTrackingTouch: ((seekBar: SeekBar?) -> Unit)? = null

    fun _onProgressChanged(func: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)) {
        _onProgressChanged = func
    }

    fun _onStartTrackingTouch(func: ((seekBar: SeekBar?) -> Unit)) {
        _onStartTrackingTouch = func
    }

    fun _onStopTrackingTouch(func: ((seekBar: SeekBar?) -> Unit)) {
        _onStopTrackingTouch = func
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        _onProgressChanged?.invoke(seekBar, progress, fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        _onStartTrackingTouch?.invoke(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        _onStopTrackingTouch?.invoke(seekBar)
    }

}


inline fun SeekBar._setOnSeekBarChangeListener(func: (SeekBarChangeListenerBridge.() -> Unit)) =
    setOnSeekBarChangeListener(SeekBarChangeListenerBridge().apply(func))


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


inline fun AbsListView._setOnScrollListener(func: (AbsListViewOnScrollListenerBridge.() -> Unit)) =
    setOnScrollListener(AbsListViewOnScrollListenerBridge().apply(func))


class TextWatcherBridge : TextWatcher {

    private var _afterTextChanged: ((text: Editable?) -> Unit)? = null
    private var _beforeTextChanged: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var _onTextChanged: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    fun _afterTextChanged(func: ((editable: Editable?) -> Unit)) {
        _afterTextChanged = func
    }

    fun _beforeTextChanged(func: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)) {
        _beforeTextChanged = func
    }

    fun _onTextChanged(func: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)) {
        _onTextChanged = func
    }

    override fun afterTextChanged(editable: Editable?) {
        _afterTextChanged?.invoke(editable)
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(text, start, count, after)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(text, start, before, count)
    }
}

inline fun TextView._addTextChangedListener(func: (TextWatcherBridge.() -> Unit)) =
    addTextChangedListener(TextWatcherBridge().apply(func))