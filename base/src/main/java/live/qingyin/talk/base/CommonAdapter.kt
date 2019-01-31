package live.qingyin.talk.base

import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by zchu on 16-12-1.
 */

abstract class CommonAdapter<T> @JvmOverloads constructor(layoutResId: Int = 0, data: List<T>? = null) :
    BaseQuickAdapter<T, CommonViewHolder>(layoutResId, data) {


    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): CommonViewHolder {
        return this.createBaseViewHolder(this.getItemView(layoutResId, parent))
    }

    override fun createBaseViewHolder(view: View): CommonViewHolder {
        return CommonViewHolder(view)
    }
}
