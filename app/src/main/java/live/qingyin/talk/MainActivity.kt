package live.qingyin.talk

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.zchu.bridge._subscribe
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx.bindLifecycle
import com.github.zchu.common.util.argument
import com.github.zchu.listing.ListingObserver
import com.github.zchu.listing.ListingView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import live.qingyin.talk.base.CommonAdapter
import live.qingyin.talk.base.CommonViewHolder
import live.qingyin.talk.presentation.login.LoginActivity
import org.koin.android.viewmodel.ext.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val str: String by argument("test")

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        showToastShort(item.itemId.toString())
        //ToastDef.showLong(item.itemId.toString(), appContext())
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                //  showAppSystemSetting()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                // showAppMarket()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    val myViewModel: MyListingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        listing_view.setAdapter {
            val value: BaseQuickAdapter<Any, *> = object : CommonAdapter<Any>(data = it) {
                override fun convert(helper: CommonViewHolder, item: Any?) {
                    (helper.itemView as TextView).text = item.toString()

                }

                override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
                    return CommonViewHolder(TextView(parent.context))
                }

            }
            value
        }
        listing_view.setOnLoadMoreListener {
            myViewModel.getListing().value?.loadMore?.invoke()
        }
        listing_view.setOnRefreshListener {
            myViewModel.getListing().value?.refresh?.invoke()

        }
        listing_view.setOnRetryListener {
            myViewModel.getListing().value?.retry?.invoke()

        }
        myViewModel
            .getListing().observe(this, ListingObserver(listing_view as ListingView<String>) {
                it.localizedMessage
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        Observable.just(str)
            .delay(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            ._subscribe {
                _onNext {
                    showToastShort(it)
                }
            }
            .bindLifecycle(this, Lifecycle.Event.ON_PAUSE)
    }
}
