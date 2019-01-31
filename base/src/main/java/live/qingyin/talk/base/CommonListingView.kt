package live.qingyin.talk.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.zchu.listing.ListingView
import com.github.zchu.stateful.R
import com.github.zchu.stateful.StatefulView

open class CommonListingView<M> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
), ListingView<M> {

    val swipeRefreshLayout: SwipeRefreshLayout by lazy(LazyThreadSafetyMode.NONE) {
        val layout = SwipeRefreshLayout(context)
        val a = context.theme.obtainStyledAttributes(intArrayOf(R.attr.colorAccent))
        try {
            layout.setColorSchemeColors(a.getColor(0, Color.parseColor("#8BC34A")))
        } finally {
            a.recycle()
        }

        layout
    }

    val recyclerView: RecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        createRecyclerView(context)
    }


    private val statefulView: StatefulView by lazy(LazyThreadSafetyMode.NONE) {
        createRealView(context)
    }


    var adapter: BaseQuickAdapter<M, *>? = null
        private set

    val realView: View
        get() = statefulView


    var isInflate: Boolean = false
        set(value) {
            val old = field
            field = value
            if (old != value && value) {
                inflateView()
            }
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatefulView)

        statefulView.setLoadingLayoutId(typedArray.getResourceId(R.styleable.StatefulView_loadingLayout, View.NO_ID))
        statefulView.setErrorLayoutId(typedArray.getResourceId(R.styleable.StatefulView_errorLayout, View.NO_ID))

        statefulView.errorTextViewId =
            typedArray.getResourceId(R.styleable.StatefulView_errorTextViewId, statefulView.errorTextViewId)

        statefulView.loadingTextViewId =
            typedArray.getResourceId(R.styleable.StatefulView_loadingTextViewId, statefulView.loadingTextViewId)
        statefulView.retryViewId =
            typedArray.getResourceId(R.styleable.StatefulView_retryViewId, statefulView.retryViewId)
        typedArray.recycle()

    }

    private fun inflateView() {
        val viewParent = parent
        if (viewParent != null && viewParent is ViewGroup) {
            val index = viewParent.indexOfChild(this)
            viewParent.removeViewInLayout(this)
            val layoutParams = layoutParams
            swipeRefreshLayout.addView(recyclerView)
            statefulView.setContentView(swipeRefreshLayout)
            if (layoutParams != null) {
                viewParent.addView(statefulView, index, layoutParams)
            } else {
                viewParent.addView(statefulView, index)
            }

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
        isInflate = true

        statefulView.showLoading()
    }

    override fun showInitialized() {
        isInflate = true

        statefulView.showContent()
    }

    override fun showInitializationFailed(message: String?) {
        isInflate = true

        statefulView.showError(message)
    }

    override fun showRefreshing() {
        isInflate = true

        if (!swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = true }
    }

    override fun showRefreshed() {
        isInflate = true

        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
    }

    override fun showRefreshFailed(message: String?) {
        isInflate = true

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
        }
        message?.let { Toast.makeText(realView.context, message, Toast.LENGTH_SHORT).show() }
    }

    override fun showLoadingMore() {
        isInflate = true

    }

    override fun showLoadMoreComplete() {
        isInflate = true

        adapter?.loadMoreComplete()
        recyclerView.stopScroll()
    }

    override fun showLoadMoreFailed(message: String?) {
        isInflate = true

        message?.let { Toast.makeText(realView.context, message, Toast.LENGTH_SHORT).show() }
        adapter?.loadMoreFail()
    }

    override fun showNoMore() {
        isInflate = true

        adapter?.loadMoreEnd()
    }


    override fun setInitial(initialData: List<M>) {
        isInflate = true
        val localAdapter = adapter
        if (localAdapter != null) {
            localAdapter.setNewData(initialData)
            if (recyclerView.adapter == null) {
                setAdapter(localAdapter, recyclerView)
            }
        } else {
            val provider = adapterProvider ?: throw NullPointerException(" Adapter 不能为 null,请设置 Adapter")
            val createAdapter = provider.invoke(initialData)
            adapter = createAdapter
            setAdapter(createAdapter, recyclerView)
        }
    }

    override fun addMore(moreData: List<M>) {
        isInflate = true

        adapter?.addData(moreData)
    }

    override fun setAll(allData: List<M>) {
        isInflate = true


    }

    protected open fun setAdapter(adapter: BaseQuickAdapter<M, *>, recyclerView: RecyclerView) {
        adapter.setOnLoadMoreListener({ onLoadMoreListener?.invoke() }, recyclerView)
        recyclerView.adapter = adapter
    }


    private var onLoadMoreListener: (() -> Unit)? = null

    fun setOnLoadMoreListener(block: () -> Unit) {
        onLoadMoreListener = block
    }

    fun setOnRetryListener(block: () -> Unit) {
        statefulView.onRetryListener = {
            block.invoke()
        }
    }

    fun setOnRefreshListener(block: () -> Unit) {
        swipeRefreshLayout.setOnRefreshListener(block)
    }

    private var adapterProvider: ((initialData: List<M>) -> BaseQuickAdapter<M, *>)? = null

    fun setAdapter(block: (initialData: List<M>) -> BaseQuickAdapter<M, *>) {
        adapterProvider = block
    }

    fun setAdapter(adapter: BaseQuickAdapter<M, *>) {
        this.adapter = adapter
    }


    fun setLoadingLayoutId(@LayoutRes layoutId: Int) {
        statefulView.setLoadingLayoutId(layoutId)
    }

    fun setLoadingView(view: View) {
        statefulView.setContentView(view)
    }

    fun setErrorLayoutId(layoutId: Int) {
        statefulView.setErrorLayoutId(layoutId)
    }

    fun setErrorView(view: View) {
        statefulView.setErrorView(view)
    }

}