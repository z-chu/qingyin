package live.qingyin.talk.di

import live.qingyin.talk.BuildConfig
import live.qingyin.talk.data.net.LeancloudService
import live.qingyin.talk.data.repository.UserRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Remote Web Service datasource
 */
val remoteDataSourceModule = module {
    // provided web components
    single { createOkHttpClient() }

    single { createWebService<LeancloudService>(get(), BuildConfig.LEANCLOUD_SERVER) }

    single { UserRepository(get()) }
}


fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient
        .Builder()
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
