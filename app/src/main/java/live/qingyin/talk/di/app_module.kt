package live.qingyin.talk.di

import live.qingyin.talk.data.repository.FileUploader
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.presentation.login.LoginViewModel
import live.qingyin.talk.presentation.user.ProfileSettingViewModel
import live.qingyin.talk.usersession.UserSessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { UserSessionManager(androidContext()) }

    single { UserRepository(get()) }

    single { FileUploader() }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { ProfileSettingViewModel(get(), get(), get()) }

}

val mainAppModules = listOf(appModule, remoteDataSourceModule)