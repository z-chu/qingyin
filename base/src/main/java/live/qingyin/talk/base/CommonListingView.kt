package live.qingyin.talk.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.zchu.listing.ListingView
import com.github.zchu.stateful.StatefulView

abstract class CommonListingView<M> : View, ListingView<M> {

    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var recyclerView: RecyclerView
    private lateinit var statefulView: StatefulView

    val realView: View
        get() = statefulView

    private var adapter: BaseQuickAdapter<M, *>? = null


    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflateView(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        inflateView(context)
    }

    private fun inflateView(context: Context) {
        val viewParent = parent
        if (viewParent != null && viewParent is ViewGroup) {
            val view = createRealView(context)
            val index = viewParent.indexOfChild(this)
            viewParent.removeViewInLayout(this)

            val layoutParams = layoutParams
            if (layoutParams != null) {
                viewParent.addView(view, index, layoutParams)
            } else {
                viewParent.addView(view, index)
            }
            swipeRefreshLayout = SwipeRefreshLayout(context)
            recyclerView = createRecyclerView(context)
            swipeRefreshLayout.addView(recyclerView)
            view.setContentView(swipeRefreshLayout)
            statefulView = view
        } else {
            throw IllegalStateException("${javaClass.simpleName} must have a non-null ViewGroup viewParent")
        }

    }

    protected open fun createRealView(context: Context): StatefulView = StatefulView(context)

    protected open fun createRecyclerView(context: Context): RecyclerView {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return recyclerView
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) {
    }

    override fun dispatchDraw(canvas: Canvas) {}


    override fun showInitializing() {
        statefulView.showLoading()
    }

    override fun showInitialized() {
        statefulView.showContent()
    }

    override fun showInitializationFailed(message: String?) {
        statefulView.showError(message)
    }

    override fun showRefreshing() {
        if (!swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = true }
    }

    override fun showRefreshed() {
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
    }

    override fun showRefreshFailed(message: String?) {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
        }
        message?.let { Toast.makeText(realView.context, message, Toast.LENGTH_SHORT).show() }
    }

    override fun showLoadingMore() {
    }

    override fun showLoadMoreComplete() {
        adapter?.loadMoreComplete()
        recyclerView.stopScroll()
    }

    override fun showLoadMoreFailed(message: String?) {
        message?.let { Toast.makeText(realView.context, message, Toast.LENGTH_SHORT).show() }
        adapter?.loadMoreFail()
    }

    override fun showNoMore() {
        adapter?.loadMoreEnd()
    }


    override fun setInitial(initialData: List<M>) {
        val localAdapter = adapter
        if (localAdapter != null) {
            localAdapter.setNewData(initialData)
        } else {
            val createAdapter = createAdapter(initialData)
            adapter = createAdapter
            setAdapter(createAdapter, recyclerView)
        }
    }

    override fun addMore(moreData: List<M>) {
        adapter?.addData(moreData)
    }

    override fun setAll(allData: List<M>) {

    }

    protected open fun setAdapter(adapter: BaseQuickAdapter<M, *>, recyclerView: RecyclerView) {
        adapter.setOnLoadMoreListener({ loadMore() }, recyclerView)
        recyclerView.adapter = adapter
    }

    protected open fun loadMore() {
        //TODO

    }

    protected abstract fun createAdapter(data: List<M>): BaseQuickAdapter<M, *>


}