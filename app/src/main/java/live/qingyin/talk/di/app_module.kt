package live.qingyin.talk.di

import live.qingyin.talk.user.UserManager
import org.koin.dsl.module

val appModule = module {
    single { UserManager(get()) }
}

val mainAppModules = listOf(appModule, remoteDataSourceModule)