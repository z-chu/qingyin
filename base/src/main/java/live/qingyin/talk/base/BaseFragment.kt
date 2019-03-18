package live.qingyin.talk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    /**
     * 第一次暴露在外
     */
    protected var isFirstShow = true

    /**
     * onViewCreated是否调用结束
     */
    protected var isViewCreated = false

    @LayoutRes
    open val layoutId: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutId?.let {
            inflater.inflate(it, container, false)
        } ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        if (userVisibleHint) {
            onShow(true)
            isFirstShow = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isViewCreated) {
                onShow(isFirstShow)
                isFirstShow = false
            }
        } else {
            onHide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        isFirstShow = true
    }

    /**
     * 当fragment暴露在外
     *
     * @param isFirstShow 是否为第一次显示
     */
    open fun onShow(isFirstShow: Boolean) {}


    /**
     * 当fragment隐藏
     */
    open fun onHide() {}


}