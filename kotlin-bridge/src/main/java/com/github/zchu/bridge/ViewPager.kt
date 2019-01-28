package com.github.zchu.bridge

import androidx.viewpager.widget.ViewPager

class OnPageChangeListenerBridge : ViewPager.OnPageChangeListener {

    private var _onPageScrolled: ((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)? = null

    fun _onPageScrolled(a: ((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)) {
        _onPageScrolled = a
    }

    private var _onPageSelected: ((position: Int) -> Unit)? = null

    fun _onPageSelected(b: ((position: Int) -> Unit)) {
        _onPageSelected = b
    }


    private var _onPageScrollStateChanged: ((state: Int) -> Unit)? = null

    fun _onPageScrollStateChanged(c: ((state: Int) -> Unit)) {
        _onPageScrollStateChanged = c
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        _onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        _onPageSelected?.invoke(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        _onPageScrollStateChanged?.invoke(state)
    }
}


inline fun ViewPager._addOnPageChangeListener(func: (OnPageChangeListenerBridge.() -> Unit)) =
    addOnPageChangeListener(OnPageChangeListenerBridge().apply(func))
