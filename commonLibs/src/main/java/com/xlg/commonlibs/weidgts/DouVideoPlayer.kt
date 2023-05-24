package com.xlg.commonlibs.weidgts

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.xlg.commonlibs.R
import com.xlg.commonlibs.ext.into
import java.util.Timer
import java.util.TimerTask
import kotlin.math.sqrt

/**
 * 带封面
 * Created by guoshuyu on 2017/9/3.
 */
open class DouVideoPlayer : StandardGSYVideoPlayer {
    private var mCoverImage: ImageView? = null
    private var mCoverOriginUrl: String? = null
    private var mDefaultRes = 0
    private var startX = 0f
    private var startY = 0f
    private var timer: Timer? = null
    var isPlaying = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun init(context: Context) {
        super.init(context)
        mCoverImage = findViewById<View>(R.id.thumbImage) as ImageView
        if (mThumbImageViewLayout != null && (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.visibility = VISIBLE
        }
    }

    override fun startPlayLogic() {
        super.startPlayLogic()
        isPlaying = true
    }

    override fun onVideoPause() {
        super.onVideoPause()
        isPlaying = false
    }

    override fun onVideoResume() {
        super.onVideoResume()
        isPlaying = true
    }

    private var mIsInLong = false
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return if (mTouchListener != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    timer = Timer()
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {
                            mIsInLong = true
                            post { mTouchListener!!.onLongPress() }
                        }
                    }, 500) // 按下时长设置
                    if (!mTouchListener!!.isInterrupt) {
                        gestureDetector.onTouchEvent(event)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX =
                        sqrt(((event.x - startX) * (event.x - startX) + (event.y - startY) * (event.y - startY)).toDouble())
                    if (deltaX > 20 && timer != null) { // 移动大于20像素
                        timer!!.cancel()
                        timer = null
                    }
                    if (!mTouchListener!!.isInterrupt) {
                        gestureDetector.onTouchEvent(event)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (timer != null) {
                        timer!!.cancel()
                        timer = null
                    }
                    if (!mIsInLong) {
                        if (!mTouchListener!!.isInterrupt) {
                            gestureDetector.onTouchEvent(event)
                        }
                        mTouchListener!!.onClick()
                    }
                    mIsInLong = false
                }

                else -> if (timer != null) {
                    timer!!.cancel()
                    timer = null
                }
            }
            true
        } else {
            gestureDetector.onTouchEvent(event)
            super.onTouch(v, event)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.video_layout_cover
    }

    fun loadCoverImage(url: String = "", res: Int) {
        mCoverOriginUrl = url
        mDefaultRes = res
        mCoverImage!!.into(url, placeHolder = res)
    }

    override fun isVerticalFullByVideoSize(): Boolean {
        //全屏竖屏显示
        return true
    }

    private var touchDoubleUpListener: TouchDoubleUpListener? = null
    fun setTouchDoubleUpListener(touchDoubleUpListener: TouchDoubleUpListener?) {
        this.touchDoubleUpListener = touchDoubleUpListener
    }

    interface TouchDoubleUpListener {
        fun touchDoubleListener(e: MotionEvent?)
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false
        mHideKey = false
    }

    override fun startWindowFullscreen(
        context: Context, actionBar: Boolean, statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar)
        val douVideoPlayer = gsyBaseVideoPlayer as DouVideoPlayer
        douVideoPlayer.loadCoverImage(mCoverOriginUrl ?: "", mDefaultRes)
        return gsyBaseVideoPlayer
    }

    override fun showSmallVideo(
        size: Point, actionBar: Boolean, statusBar: Boolean
    ): GSYBaseVideoPlayer {
        //下面这里替换成你自己的强制转化
        val douVideoPlayer = super.showSmallVideo(size, actionBar, statusBar) as DouVideoPlayer
        douVideoPlayer.mStartButton.visibility = GONE
        douVideoPlayer.mStartButton = null
        return douVideoPlayer
    }

    override fun cloneParams(from: GSYBaseVideoPlayer, to: GSYBaseVideoPlayer) {
        super.cloneParams(from, to)
        val sf = from as DouVideoPlayer
        val st = to as DouVideoPlayer
        st.mShowFullAnimation = sf.mShowFullAnimation
    }

    /******************* 下方两个重载方法，在播放开始前不屏蔽封面，不需要可屏蔽  */
    override fun onSurfaceUpdated(surface: Surface) {
        super.onSurfaceUpdated(surface)
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == VISIBLE) {
            mThumbImageViewLayout.visibility = INVISIBLE
        }
    }

    override fun setViewShowState(view: View, visibility: Int) {
        if (view === mThumbImageViewLayout && visibility != VISIBLE) {
            return
        }
        super.setViewShowState(view, visibility)
    }

    override fun onSurfaceAvailable(surface: Surface) {
        super.onSurfaceAvailable(surface)
        if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE) {
            if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == VISIBLE) {
                mThumbImageViewLayout.visibility = INVISIBLE
            }
        }
    }

    /**
     * 所有状态都不显示控制栏
     */
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        setViewInvisible()
    }

    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        setViewInvisible()
        setViewShowState(mStartButton, INVISIBLE)
    }

    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        setViewInvisible()
    }

    override fun changeUiToError() {
        super.changeUiToError()
        setViewInvisible()
    }

    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        setViewInvisible()
    }

    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        setViewInvisible()
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        setViewInvisible()
    }

    override fun startAfterPrepared() {
        super.startAfterPrepared()
        setViewInvisible()
    }

    private fun setViewInvisible() {
        setViewShowState(mTopContainer, INVISIBLE)
        setViewShowState(mBottomContainer, INVISIBLE)
        setViewShowState(mBottomProgressBar, VISIBLE)
        setViewShowState(mBackButton, INVISIBLE)
        setViewShowState(mStartButton, INVISIBLE)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        super.onStartTrackingTouch(seekBar)
        //拖动进度条 不显示   啥事儿都不干
    }

    private var mTouchListener: TouchListener? = null
    fun setOnTouchListener(listener: TouchListener?) {
        mTouchListener = listener
    }

    interface TouchListener {
        fun onClick()
        fun onLongPress()
        val isInterrupt: Boolean
    }
}