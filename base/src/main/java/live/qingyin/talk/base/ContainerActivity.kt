package live.qingyin.talk.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment

abstract class ContainerActivity : BaseActivity() {

    open val containerViewId: Int
        get() = R.id.container


    final override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
        val supportFragmentManager = supportFragmentManager
        if (savedInstanceState == null || supportFragmentManager.findFragmentByTag("container_fragment") == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, createFragment(), "container_fragment")
                .commit()
        }
    }

    open fun onCreated(savedInstanceState: Bundle?) {
        setContentView(R.layout.abc_activity_fragment)
    }


    abstract fun createFragment(): Fragment

}