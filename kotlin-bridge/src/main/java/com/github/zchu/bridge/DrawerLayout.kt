package com.github.zchu.bridge

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout


class DrawerListenerBridge : DrawerLayout.DrawerListener {

    private var _onDrawerStateChanged: ((newState: Int) -> Unit)? = null

    private var _onDrawerSlide: ((drawerView: View, slideOffset: Float) -> Unit)? = null

    private var _onDrawerClosed: ((drawerView: View) -> Unit)? = null

    private var _onDrawerOpened: ((drawerView: View) -> Unit)? = null

    fun _onDrawerStateChanged(t: ((newState: Int) -> Unit)) {
        _onDrawerStateChanged = t
    }

    fun _onDrawerSlide(t: ((drawerView: View, slideOffset: Float) -> Unit)) {
        _onDrawerSlide = t
    }

    fun _onDrawerClosed(t: ((drawerView: View) -> Unit)) {
        _onDrawerClosed = t
    }

    fun _onDrawerOpened(t: ((drawerView: View) -> Unit)) {
        _onDrawerOpened = t
    }

    override fun onDrawerStateChanged(newState: Int) {
        _onDrawerStateChanged?.invoke(newState)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        _onDrawerSlide?.invoke(drawerView, slideOffset)
    }

    override fun onDrawerClosed(drawerView: View) {
        _onDrawerClosed?.invoke(drawerView)
    }


    override fun onDrawerOpened(drawerView: View) {
        _onDrawerOpened?.invoke(drawerView)
    }

}

inline fun DrawerLayout._addDrawerListener(func: DrawerListenerBridge.() -> Unit) =
    addDrawerListener(DrawerListenerBridge().apply(func))
