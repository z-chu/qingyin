package live.qingyin.talk.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat

class SettingRow @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val ivIcon: ImageView
    private val tvTitle: TextView
    private val tvSubtitle: TextView
    private val divider: View
    private val arrow: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_setting_row, this, true)
        ivIcon = findViewById(R.id.iv_icon)
        tvTitle = findViewById(R.id.tv_title)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        divider = findViewById(R.id.divider)
        arrow = findViewById(R.id.arrow)
        val layoutParams = layoutParams
        if (layoutParams == null || layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setLayoutParams(
                ViewGroup.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(
                        R.dimen.setting_row_height
                    )
                )
            )
        }
        if (attrs != null) {
            initAttrs(context, attrs)
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingRow)
        val iconResId = typedArray.getResourceId(R.styleable.SettingRow_setting_icon, 0)
        if (iconResId != 0) {
            ivIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, iconResId, context.theme))
        } else {
            ivIcon.visibility = View.GONE
            (tvTitle.layoutParams as RelativeLayout.LayoutParams).leftMargin =
                (ivIcon.layoutParams as RelativeLayout.LayoutParams).leftMargin
        }
        val title = typedArray.getString(R.styleable.SettingRow_setting_title)
        tvTitle.text = title
        val titleBold = typedArray.getBoolean(R.styleable.SettingRow_setting_title_bold, false)
        tvTitle.paint.isFakeBoldText = titleBold
        val subtitle = typedArray.getString(R.styleable.SettingRow_setting_subtitle)
        tvSubtitle.text = subtitle
        val dividerVisibility = typedArray.getBoolean(R.styleable.SettingRow_setting_divider_visibility, true)
        divider.visibility = if (dividerVisibility) View.VISIBLE else View.GONE
        val arrowVisibility = typedArray.getBoolean(R.styleable.SettingRow_setting_arrow_visibility, true)
        val layoutParams = tvSubtitle.layoutParams as RelativeLayout.LayoutParams
        if (arrowVisibility) {
            arrow.visibility = View.VISIBLE
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            layoutParams.rightMargin = 0
        } else {
            arrow.visibility = View.GONE
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            layoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.setting_row_horizontal_margin)
        }
        tvTitle.setTextColor(
            typedArray.getColor(
                R.styleable.SettingRow_setting_title_color,
                Color.parseColor("#212121")
            )
        )
        tvSubtitle.setTextColor(
            typedArray.getColor(
                R.styleable.SettingRow_setting_subtitle_color,
                Color.parseColor("#9192a6")
            )
        )
        arrow.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                typedArray.getResourceId(R.styleable.SettingRow_setting_arrow, R.drawable.ic_setting_row_arrow),
                context.theme
            )
        )
        divider.background =
            typedArray.getDrawable(R.styleable.SettingRow_setting_divider) ?: ColorDrawable(Color.parseColor("#eeeeee"))
        typedArray.recycle()
    }


    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    fun setSubtitle(subtitle: String?) {
        tvSubtitle.text = subtitle
    }

    fun setSubtitleTextColor(@ColorInt color: Int) {
        tvSubtitle.setTextColor(color)
    }

    fun setSubtitleVisibility(visibility: Boolean) {
        tvSubtitle.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    fun setDividerVisibility(visibility: Boolean) {
        divider.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    fun setArrowVisibility(visibility: Boolean) {
        arrow.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    fun setTitleBold(isBold: Boolean) {
        tvTitle.paint.isFakeBoldText = isBold
    }
}