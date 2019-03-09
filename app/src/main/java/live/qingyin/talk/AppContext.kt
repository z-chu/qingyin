package live.qingyin.talk

import android.app.Application
import android.content.Context
import com.github.zchu.common.help.ToastDef
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {


}

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
        ToastDef.defaultContext = this
        startKoin {
            logger()
            androidContext(this@AppContext)
            modules(appModule)
        }

    }
}