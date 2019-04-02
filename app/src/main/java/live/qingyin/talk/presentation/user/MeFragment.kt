package live.qingyin.talk.presentation.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.zchu.common.help.BaseFragmentAdapter
import com.github.zchu.common.util.setDebounceOnClickLister
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.header_user_overview.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.presentation.setting.SettingActivity
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.GlideApp
import org.koin.android.ext.android.inject

class MeFragment : BaseFragment() {

    private val userSessionManager: UserSessionManager by inject()

    override val layoutId: Int?
        get() = R.layout.fragment_me

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_setting -> SettingActivity.start(requireContext())
            }
            return@setOnMenuItemClickListener true
        }
        header_user_overview.setDebounceOnClickLister {
            ProfileSettingsActivity.start(requireContext())
        }

        val fragmentAdapter = BaseFragmentAdapter(childFragmentManager)
        fragmentAdapter.fragments = arrayOf(SoundFragment(), FeedFragment(), AboutFragment())
        fragmentAdapter.titles =
            arrayOf(
                getString(R.string.sound),
                getString(R.string.feed),
                getString(R.string.about_users, getString(R.string.title_me))
            )
        viewpager.adapter = fragmentAdapter
        tab_layout.setupWithViewPager(viewpager)

        userSessionManager
            .liveDataOfUser()
            .observe(this, Observer {
                if (it != null) {
                    displayUser(it)
                }
            })
    }

    private fun displayUser(user: UserSession) {
        tv_name.text = user.name
        tv_bio.text = user.bio ?: getString(R.string.default_bio_if_null)
        val profilePhoto = user.profilePhoto
        if (profilePhoto == null) {
            GlideApp
                .with(this)
                .load(R.drawable.img_profile_photo_default)
                .into(iv_profile_photo)
        } else {
            GlideApp
                .with(this)
                .load(profilePhoto)
                .transform(CircleCrop())
                .into(iv_profile_photo)
        }

        val cover = user.cover
        if (cover == null) {
            GlideApp
                .with(this)
                .load(R.drawable.img_cover_default)
                .into(iv_cover)
        } else {
            GlideApp
                .with(this)
                .load(cover)
                .into(iv_cover)
        }
    }

}