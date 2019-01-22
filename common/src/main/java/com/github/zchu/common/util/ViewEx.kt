package com.github.zchu.common.util

import android.view.View

//filter frequent click event
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