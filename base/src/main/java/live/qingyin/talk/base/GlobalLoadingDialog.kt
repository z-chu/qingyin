package live.qingyin.talk.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle

class GlobalLoadingDialog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}