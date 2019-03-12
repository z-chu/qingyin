package live.qingyin.talk.di

import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.presentation.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { live.qingyin.talk.usersession.UserManager(get()) }

    single { UserRepository(get()) }

    viewModel { LoginViewModel(get(), get()) }

}

val mainAppModules = listOf(appModule, remoteDataSourceModule)