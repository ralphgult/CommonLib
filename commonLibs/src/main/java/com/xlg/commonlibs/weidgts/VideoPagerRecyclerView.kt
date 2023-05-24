package com.xlg.commonlibs.weidgts

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager
import com.xlg.commonlibs.base.BaseListData

class VideoPagerRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RefreshRecyclerView(context, attrs, defStyleAttr) {
    private var currentPos = 0
    private var mVideoControlListener: VideoControlListener? = null
    override fun onRecyclerViewCreated() {
        super.onRecyclerViewCreated()
        val layoutManager = ViewPagerLayoutManager(context, OrientationHelper.VERTICAL)
        layoutManager.scrollToPositionWithOffset(0, 0)
        layoutManager.stackFromEnd = true
        layoutManager.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
//                mVideoControlListener?.playVideo(0, mRecycler)
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
//                val index: Int = if (isNext) {
//                    0
//                } else {
//                    1
//                }
//                mVideoControlListener?.stopVideo(index, mRecycler)
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
//                if (currentPos != position) {
//                    mVideoControlListener?.playVideo(position, mRecycler)
//                }
                currentPos = position
            }
        })
        mRecycler.layoutManager = layoutManager
    }

    fun addControlListener(listener: VideoControlListener){
        mVideoControlListener = listener
    }

    interface VideoControlListener {
        fun playVideo(position: Int, recyclerView: RecyclerView)
        fun stopVideo(position: Int, recyclerView: RecyclerView)
    }
}