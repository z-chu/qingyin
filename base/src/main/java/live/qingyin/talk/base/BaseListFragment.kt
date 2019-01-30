package live.qingyin.talk.base

import androidx.fragment.app.Fragment
import com.github.zchu.listing.ListingView

class BaseListFragment<T> : Fragment(), ListingView<T> {


    override fun showInitializing() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showInitialized() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showInitializationFailed(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRefreshing() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRefreshed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRefreshFailed(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadMoreComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadMoreFailed(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setInitial(initialData: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMore(moreData: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAll(allData: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}