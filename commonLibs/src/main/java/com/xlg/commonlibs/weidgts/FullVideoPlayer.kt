package com.xlg.commonlibs.weidgts

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.xlg.commonlibs.R
import com.xlg.commonlibs.ext.click
import com.xlg.commonlibs.ext.getVideoUrl
import com.xlg.commonlibs.ext.into
import com.xlg.commonlibs.ext.setGone

class FullVideoPlayer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    StandardGSYVideoPlayer(context, attrs) {
    private var mIsPlaying = false
    private lateinit var progressCallback: (progress: Long) -> Unit
    fun setUrl(url: String, cover: String) {
        val imageView = AppCompatImageView(context)
        val height = resources.getDimensionPixelOffset(R.dimen.dp_210)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, height)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.into(cover)
        imageView.layoutParams = params
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption
            .setThumbImageView(imageView)
            .setShowPauseCover(false)
            .setRotateViewAuto(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(true)
            .setNeedLockFull(false)
            .setThumbImageView(imageView)
            .setUrl(url.getVideoUrl())
            .setCacheWithPlay(false)
            .setGSYVideoProgressListener { progress, _, _, _ ->
                if (this::progressCallback.isInitialized) {
                    progressCallback(progress)
                }
            }
            .build(this)
        mBackButton.setGone()
        mTopContainer.setGone()
        mBottomContainer.setGone()
        mProgressBar.setGone()
        mFullscreenButton.setGone()
        mCurrentTimeTextView.setGone()
        mTotalTimeTextView.setGone()
        hideAllWidget()
        click {
            if (mIsPlaying) {
                onVideoPause()
            } else {
                onVideoResume()
            }
        }
    }

    fun setProgressCallback(callback: (progress: Long) -> Unit) {
        progressCallback = callback
    }

}