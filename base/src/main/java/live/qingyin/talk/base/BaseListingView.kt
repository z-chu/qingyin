package live.qingyin.talk.base

import android.content.Context
import android.util.AttributeSet

open class BaseListingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CommonListingView<Any>(context, attrs, defStyleAttr)