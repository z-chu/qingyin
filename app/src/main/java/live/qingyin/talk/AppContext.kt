package live.qingyin.talk

import android.app.Application
import android.content.Context
import com.github.zchu.common.help.ToastDef
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {


    // MyViewModel ViewModel
    viewModel { MyListingViewModel() }
}

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
        startKoin {
            logger()
            androidContext(this@AppContext)
            modules(appModule)
        }

    }
}