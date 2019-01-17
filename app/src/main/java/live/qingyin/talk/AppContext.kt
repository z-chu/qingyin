package live.qingyin.talk

import android.app.Application
import com.github.zchu.common.util.ToastDef

private lateinit var context: AppContext

fun appContext(): AppContext = context

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        ToastDef.defaultContext = this

    }
}