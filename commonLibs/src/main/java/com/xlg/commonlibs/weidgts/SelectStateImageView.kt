package com.xlg.commonlibs.weidgts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.xlg.commonlibs.R
import kotlinx.coroutines.selects.select

@SuppressLint("Recycle")
class SelectStateImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var selectedRes: Int = 0
    private var normalRes: Int = 0

    init {
        attrs?.apply {
            val array = context.obtainStyledAttributes(this, R.styleable.SelectStateImageView)
            selectedRes = array.getResourceId(R.styleable.SelectStateImageView_selectedImg, 0)
            normalRes = array.getResourceId(R.styleable.SelectStateImageView_normalImg, 0)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageResource(normalRes)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        setImageResource(
            if (selected) {
                selectedRes
            } else {
                normalRes
            }
        )
    }

    fun setSelectedImg(resId: Int) {
        selectedRes = resId
        if (isSelected) {
            setImageResource(resId)
        }
    }

    fun setNormalImg(resId: Int) {
        normalRes = resId
        if (!isSelected) {
            setImageResource(resId)
        }
    }
}