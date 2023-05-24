package com.xlg.commonlibs.ext

import android.app.Application
import android.view.ViewGroup
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.model.VideoOptionModel
import com.xlg.commonlibs.base.BaseViewHolder
import com.xlg.commonlibs.utils.CommonListAdapter
import com.xlg.commonlibs.utils.FunctionInterface
import com.xlg.commonlibs.utils.TYPE_IS_DEBUG
import com.xlg.commonlibs.utils.TYPE_SERVER_PATH
import tv.danmaku.ijk.media.player.IjkMediaPlayer


private var callback: FunctionInterface? = null
lateinit var app: Application

fun initLib(application: Application, inter: FunctionInterface) {
    app = application
    callback = inter
    setVideoPlayer()

}

val serverPath: String
    get() = callback?.function(TYPE_SERVER_PATH) as String

val debug: Boolean
    get() = (callback?.function(TYPE_IS_DEBUG) as? Boolean) ?: true

val packageName: String
    get() = app.packageName

fun getHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
    callback?.getViewHolder(parent, viewType) ?: CommonListAdapter.DefaultViewHolder(parent, 0)

/**
 * 设置视频播放参数
 */
private fun setVideoPlayer() {
    val list = ArrayList<VideoOptionModel>()
    //此中内容：优化加载速度，降低延迟
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp"))
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp"))
    list.add(
        VideoOptionModel(
            IjkMediaPlayer.OPT_CATEGORY_FORMAT,
            "allowed_media_types",
            "video"
        )
    ) //根据媒体类型来配置
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 1316))
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1)) // 无限读
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100))
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 10240))
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1))
    //  关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
    list.add(VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0))
    GSYVideoManager.instance().optionModelList = list
}

