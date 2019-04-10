package live.qingyin.talk.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.api.ChuckCollector
import com.readystatesoftware.chuck.api.ChuckInterceptor
import com.readystatesoftware.chuck.api.RetentionManager
import live.qingyin.talk.BuildConfig
import live.qingyin.talk.data.net.H_LC_ID
import live.qingyin.talk.data.net.H_LC_KEY
import live.qingyin.talk.data.net.HeaderInterceptor
import live.qingyin.talk.data.net.LeancloudService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Remote Web Service datasource
 */
val remoteDataSourceModule = module {

    single { createChuckCollector(androidContext()) }

    single { createChuckInterceptor(androidContext(), get()) }

    // provided web components
    single { createOkHttpClient() }

    single { createWebService<LeancloudService>(get(), BuildConfig.LEANCLOUD_SERVER) }

}

fun createChuckCollector(context: Context): ChuckCollector {
    return ChuckCollector(context)
        .showNotification(true)
        .retentionManager(RetentionManager(context, ChuckCollector.Period.ONE_HOUR))
}

fun createChuckInterceptor(context: Context, collector: ChuckCollector): ChuckInterceptor {
    return ChuckInterceptor(context, collector)
        .maxContentLength(250000L)
}

private class ChuckComponent : KoinComponent {
    val chuckInterceptor: ChuckInterceptor by inject()
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    remoteDataSourceModule
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient
        .Builder()
        .addInterceptor(
            HeaderInterceptor(
                hashMapOf(
                    H_LC_ID to BuildConfig.LEANCLOUD_APP_ID,
                    H_LC_KEY to BuildConfig.LEANCLOUD_APP_KEY
                )
            )
        )
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(ChuckComponent().chuckInterceptor)
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(T::class.java)
}
