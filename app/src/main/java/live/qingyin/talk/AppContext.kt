package live.qingyin.talk

import android.app.Application
import android.content.Context
import com.github.zchu.common.help.ToastDef
import live.qingyin.talk.di.mainAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


private lateinit var context: AppContext

val appContext: AppContext
    get() = context

class AppContext : Application() {

    override fun attachBaseContext(base: Context?) {
        context = this
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger()
            androidContext(this@AppContext)
            modules(mainAppModules)
        }
        ToastDef.defaultContext = this

    }
}