package live.qingyin.talk.di

import android.content.Context
import com.readystatesoftware.chuck.api.ChuckCollector
import com.readystatesoftware.chuck.api.ChuckInterceptor
import com.readystatesoftware.chuck.api.RetentionManager
import live.qingyin.talk.BuildConfig
import live.qingyin.talk.data.net.LeancloudService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Remote Web Service datasource
 */
val remoteDataSourceModule = module {

    single { createChuckCollector(get()) }

    single { createChuckInterceptor(get(), get()) }

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

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient
        .Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
