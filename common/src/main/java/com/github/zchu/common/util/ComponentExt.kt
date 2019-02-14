@file:Suppress("UNCHECKED_CAST")

package com.github.zchu.common.util

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Retrieve argument from Activity intent
 */
fun <T : Any> Activity.argument(key: String, defaultValue: T? = null) =
    lazy { intent.extras?.get(key) as? T ?: defaultValue ?: error("Intent Argument $key is missing") }


/**
 * Retrieve argument from Fragment intent
 */
fun <T : Any> Fragment.argument(key: String, defaultValue: T? = null) =
    lazy { arguments?.get(key) as? T ?: defaultValue ?: error("Intent Argument $key is missing") }

