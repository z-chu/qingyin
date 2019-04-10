package live.qingyin.talk.presentation.user

import android.app.Activity
import android.content.Intent
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.fragment.app.Fragment
import java.util.*

class ActivityResultDispatcher {

    private val subscribeCallbacks = SparseArray<LinkedList<((resultCode: Int, data: Intent?) -> Unit)>>()
    private val observeCallbacks = ArrayList<((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)>()
    private val codeGenerator = Random()

    private fun generateRequestCode(): Int {
        var requestCode: Int
        do {
            requestCode = codeGenerator.nextInt(0xFFFF)
        } while (subscribeCallbacks.containsKey(requestCode))
        return requestCode
    }

    fun subscribe(requestCode: Int, callback: ((resultCode: Int, data: Intent?) -> Unit)) {
        var linkedList = subscribeCallbacks.get(requestCode)
        if (linkedList == null) {
            linkedList = LinkedList()
            subscribeCallbacks.put(requestCode, linkedList)
        }
        linkedList.add(callback)
    }


    /**
     * 接受到一次结果后，取消订阅
     */
    fun subscribeOnce(requestCode: Int, callback: ((resultCode: Int, data: Intent?) -> Unit)) {
        subscribe(requestCode) { resultCode, data ->
            callback.invoke(resultCode, data)
            subscribeCallbacks.remove(requestCode)
        }
    }

    fun observe(callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)) {
        observeCallbacks.add(callback)
    }

    fun startIntent(activity: Activity, intent: Intent, callback: ((resultCode: Int, data: Intent?) -> Unit)) {
        val resultCode = generateRequestCode()
        activity.startActivityForResult(intent, resultCode)
        subscribeOnce(resultCode, callback)
    }

    fun startIntent(fragment: Fragment, intent: Intent, callback: ((resultCode: Int, data: Intent?) -> Unit)) {
        val requestCode = generateRequestCode()
        fragment.startActivityForResult(intent, requestCode)
        subscribeOnce(requestCode, callback)
    }

    fun dispatch(requestCode: Int, resultCode: Int, data: Intent?) {
        for (callback in observeCallbacks) {
            callback.invoke(requestCode, resultCode, data)
        }
        val linkedList = subscribeCallbacks.get(requestCode)
        if (linkedList != null) {
            for (function in linkedList) {
                function.invoke(resultCode, data)
            }
        }
    }
}