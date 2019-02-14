package com.github.zchu.mvp

interface MvpView

fun <V : MvpView, P : MvpPresenter2<V>> V.attachToPresenter(presenter: P): P {
    presenter.attach(this)
    return presenter
}
