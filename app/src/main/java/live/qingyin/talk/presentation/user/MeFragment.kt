package live.qingyin.talk.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.zchu.common.util.dp2px
import kotlinx.android.synthetic.main.header_user_overview.*
import live.qingyin.talk.R
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.GlideApp
import org.koin.android.ext.android.inject

class MeFragment : Fragment() {

    val userSessionManager: UserSessionManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager
                .liveDataOfUser()
                .observe(this, Observer {
                    if (it != null) {
                        displayUser(it)
                    }
                })
    }


    private fun displayUser(user: UserSession) {
        tv_name.text = user.username
        tv_bio.text = "在漫天风沙里望着你远去,我竟悲伤地不能自己"
        GlideApp
                .with(this)
                .load("https://avatars3.githubusercontent.com/u/13902657?s=460&v=4")
                .transform(RoundedCorners(requireContext().dp2px(6F)))
                .into(iv_profile_photo)

        GlideApp
                .with(this)
                .load("http://img1.imgtn.bdimg.com/it/u=2502073311,3044549660&fm=26&gp=0.jpg")
                .into(iv_cover)
    }

}