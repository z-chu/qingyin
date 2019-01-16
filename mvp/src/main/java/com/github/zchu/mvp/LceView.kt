package com.github.zchu.mvp

interface LceView : MvpView {
    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 显示加载出错
     *
     * @param errorMsg  异常信息
     */
    fun showError(errorMsg: String)

}
