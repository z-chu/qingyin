package live.qingyin.talk

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.github.zchu.common.help.ToastDef
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
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
        initLogger()
        ToastDef.defaultContext = this

    }


    fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("qingyin")
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        Logger.addLogAdapter(object : DiskLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                if (BuildConfig.DEBUG) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 检查是否有储存空间权限
                        val checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        return checkSelfPermission != PackageManager.PERMISSION_GRANTED
                    }
                    return true
                }

                return false
            }
        })

    }
}