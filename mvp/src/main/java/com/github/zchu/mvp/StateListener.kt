package com.github.zchu.mvp

import android.os.Bundle

interface StateListener {

    val bundleKey: String

    fun onRestoreState(inState: Bundle?)

    fun onSaveState(outState: Bundle)
}
