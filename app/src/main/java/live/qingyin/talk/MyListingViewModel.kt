package live.qingyin.talk

import com.github.zchu.listing.AbsListingModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyListingViewModel : AbsListingModel<String>() {

    override fun doLoadInitial(isRefresh: Boolean, callback: LoadCallback<String>) {
        Schedulers.io().scheduleDirect {
            Thread.sleep(3000)
            AndroidSchedulers.mainThread().scheduleDirect {


                    callback.onResult(
                        arrayListOf(
                            "1",
                            "2",
                            "3",
                            "4",
                            "5",
                            "6",
                            "7",
                            "8",
                            "9",
                            "10",
                            "11",
                            "12",
                            "13"
                        )
                    )

            }
        }
    }

    override fun doLoadMore(callback: LoadCallback<String>) {
        Schedulers.io().scheduleDirect {
            Thread.sleep(1000)
            AndroidSchedulers.mainThread().scheduleDirect {

                callback.onResult(
                        arrayListOf(
                            "1m",
                            "2m",
                            "3m",
                            "4m",
                            "5m",
                            "6m",
                            "7m",
                            "8m",
                            "9m"
                        )
                    )


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