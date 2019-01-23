package com.github.zchu.common.weiget

import android.widget.SeekBar

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
