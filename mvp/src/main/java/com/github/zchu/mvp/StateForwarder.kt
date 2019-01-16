package com.github.zchu.mvp

import android.os.Bundle
import java.util.*

class StateForwarder {
    private val stateListeners = HashSet<StateListener>()
    private var inState: Bundle? = null

    fun addListener(listener: StateListener?) {
        if (listener == null) return
        val add = stateListeners.add(listener)
        if (add) {
            inState?.let {
                val bundle = it.getBundle(listener.bundleKey)
                listener.onRestoreState(bundle)
            }
        }
    }

    fun restoreState(savedInstanceState: Bundle?) {
        this.inState = savedInstanceState
        if (savedInstanceState != null) {
            for (stateListener in stateListeners) {
                val bundle = savedInstanceState.getBundle(stateListener.bundleKey)
                stateListener.onRestoreState(bundle)
            }
        }

    }

    fun saveInstanceState(outState: Bundle?) {
        if (outState != null) {
            for (stateListener in stateListeners) {
                val bundle = Bundle()
                stateListener.onSaveState(bundle)
                outState.putBundle(stateListener.bundleKey, bundle)
            }
        }
    }

}
