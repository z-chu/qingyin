package live.qingyin.talk.rx

import android.app.Dialog
import android.content.Context
import androidx.annotation.MainThread
import com.github.zchu.common.help.showToastShort
import io.reactivex.disposables.Disposable
import live.qingyin.talk.R
import live.qingyin.talk.base.GlobalLoadingDialog

/**
 * Created by zchu on 17-2-23.
 */

abstract class ProgressSubscriber<T> @MainThread
@JvmOverloads constructor(context: Context, private val mCancelable: Boolean = true) :
    com.github.zchu.common.rx.ProgressSubscriber<T>() {

    private var mContext: Context? = context

    override fun createProgressDialog(disposable: Disposable): Dialog {
        val loadingDialog = GlobalLoadingDialog(mContext!!)
        loadingDialog.setCancelable(mCancelable)
        if (mCancelable) {
            loadingDialog.setOnCancelListener {
                mContext?.let {
                    it.showToastShort(it.getString(R.string.cancelled))
                }

                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }
            loadingDialog.setOnDismissListener { mContext = null }

        }
        return loadingDialog
    }
}
