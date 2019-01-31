package live.qingyin.talk

import com.github.zchu.listing.AbsListingModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyListingViewModel : AbsListingModel<String>() {

    override fun doLoadInitial(isRefresh: Boolean, callback: LoadCallback<String>) {
        Schedulers.io().scheduleDirect {
            Thread.sleep(3000)
            AndroidSchedulers.mainThread().scheduleDirect {

                if (System.currentTimeMillis() % 2 == 0L) {
                    callback.onFailure(NullPointerException("出bug啦"))
                } else {
                    callback.onResult(
                        arrayListOf(
                            "asda",
                            "asdaf",
                            "adsadad",
                            "asdadzsc",
                            "ascaga",
                            "sadsadasasdaf",
                            "adsaczg",
                            "039rjians",
                            "asdkma,g",
                            "adsadad",
                            "asdadzsc",
                            "ascaga",
                            "sadsadasd"
                        )
                    )
                }
            }
        }
    }

    override fun doLoadMore(callback: LoadCallback<String>) {
        Schedulers.io().scheduleDirect {
            Thread.sleep(1000)
            AndroidSchedulers.mainThread().scheduleDirect {
                if (System.currentTimeMillis() % 2 == 0L) {
                    callback.onFailure(NullPointerException("出bug啦"))
                } else {
                    callback.onResult(
                        arrayListOf(
                            "asda",
                            "asdaf",
                            "adsaczg",
                            "039rjians",
                            "asdkma,g",
                            "adsadad",
                            "asdadzsc",
                            "ascaga",
                            "sadsadasd"
                        )
                    )

                }


            }
        }
    }

    override fun isNoMoreOnInitial(initialData: List<String>): Boolean {

        return getDataSize() > 100

    }

    override fun isNoMoreOnMore(moreData: List<String>): Boolean {
        return getDataSize() > 100

    }
}