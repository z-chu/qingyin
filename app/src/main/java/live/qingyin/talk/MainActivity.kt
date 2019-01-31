package live.qingyin.talk

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.zchu.bridge._subscribe
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx.bindLifecycle
import com.github.zchu.listing.SuperListingPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import live.qingyin.talk.base.CommonAdapter
import live.qingyin.talk.base.CommonViewHolder
import live.qingyin.talk.presentation.login.LoginActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


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

    val superListingPresenter: SuperListingPresenter<String> by lazy {
        val superListingPresenter1 = SuperListingPresenter(listing_view, this) as SuperListingPresenter<String>
        listing_view.setOnLoadMoreListener {
            superListingPresenter1.loadMore()
        }
        listing_view.setOnRefreshListener {
            superListingPresenter1.refresh()
        }
        listing_view.setOnRetryListener {
            superListingPresenter1.retry()
        }
        superListingPresenter1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        Observable.just("柱子哥牛逼")
            .delay(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            ._subscribe {
                _onNext {
                }
            }
            .bindLifecycle(this, Lifecycle.Event.ON_STOP)
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
        ViewModelProviders.of(this).get(MyListingViewModel::class.java)
            .getListing().observe(this, Observer {
                superListingPresenter.onResource(it)
            })

    }
}
