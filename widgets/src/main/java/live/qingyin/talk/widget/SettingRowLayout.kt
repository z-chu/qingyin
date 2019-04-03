package live.qingyin.talk.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat

class SettingRowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.settingRowLayoutStyle
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
        initAttrs(context, attrs)
        initBackground()
    }

    private fun initBackground() {
        if (background == null) {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            val attribute = intArrayOf(android.R.attr.selectableItemBackground)
            val typedArray = context.theme.obtainStyledAttributes(typedValue.resourceId, attribute)
            val drawable = typedArray.getDrawable(0)
            background = drawable
            typedArray.recycle()
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingRowLayout)
        val iconResId = typedArray.getResourceId(R.styleable.SettingRowLayout_setting_icon, 0)
        if (iconResId != 0) {
            ivIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, iconResId, context.theme))
        } else {
            ivIcon.visibility = View.GONE
            (tvTitle.layoutParams as RelativeLayout.LayoutParams).leftMargin =
                (ivIcon.layoutParams as RelativeLayout.LayoutParams).leftMargin
        }
        val title = typedArray.getString(R.styleable.SettingRowLayout_setting_title)
        tvTitle.text = title
        val titleBold = typedArray.getBoolean(R.styleable.SettingRowLayout_setting_title_bold, false)
        tvTitle.paint.isFakeBoldText = titleBold
        val subtitle = typedArray.getString(R.styleable.SettingRowLayout_setting_subtitle)
        tvSubtitle.text = subtitle
        val dividerVisibility = typedArray.getBoolean(R.styleable.SettingRowLayout_setting_divider_visibility, true)
        setDividerVisibility(dividerVisibility)
        val arrowVisibility = typedArray.getBoolean(R.styleable.SettingRowLayout_setting_arrow_visibility, true)
        setArrowVisibility(arrowVisibility)

        arrow.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                typedArray.getResourceId(R.styleable.SettingRowLayout_setting_arrow, R.drawable.ic_setting_row_arrow),
                context.theme
            )
        )
        typedArray
            .getColor(
                R.styleable.SettingRowLayout_setting_title_color,
                -1
            )
            .let {
                if (it != -1) {
                    tvTitle.setTextColor(it)
                }
            }
        typedArray
            .getColor(
                R.styleable.SettingRowLayout_setting_subtitle_color,
                -1
            )
            .let {
                if (it != -1) {
                    tvSubtitle.setTextColor(it)
                }
            }
        typedArray
            .getDrawable(R.styleable.SettingRowLayout_setting_divider)
            .let {
                if (it != null) {
                    divider.background = it
                }
            }

        typedArray.recycle()
    }


    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            val layoutParams = layoutParams
            if (layoutParams == null) {
                setLayoutParams(
                    ViewGroup.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        resources.getDimensionPixelSize(
                            R.dimen.setting_row_height
                        )
                    )
                )
            } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = resources.getDimensionPixelSize(
                    R.dimen.setting_row_height
                )
            }
        }
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
        val layoutParams = tvSubtitle.layoutParams as RelativeLayout.LayoutParams
        if (visibility) {
            arrow.visibility = View.VISIBLE
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            layoutParams.rightMargin = 0
        } else {
            arrow.visibility = View.GONE
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            layoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.setting_row_horizontal_margin)
        }
    }

    fun setTitleBold(isBold: Boolean) {
        tvTitle.paint.isFakeBoldText = isBold
    }
}