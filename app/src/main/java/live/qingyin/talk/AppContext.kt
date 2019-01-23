package live.qingyin.talk

import android.app.Application
import android.content.Context
import com.github.zchu.common.help.ToastDef

private lateinit var context: AppContext

fun appContext(): AppContext = context

class AppContext : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        ToastDef.defaultContext = this

    }
}