package live.qingyin.talk

import android.app.Application

private lateinit var context: AppContext

val appContext: AppContext = context

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}