package com.logex.headlinenews.ui.video

import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import com.logex.headlinenews.R
import com.logex.headlinenews.base.MVVMFragment
import com.logex.utils.StatusBarUtil
import com.logex.videoplayer.JCVideoPlayer
import kotlinx.android.synthetic.main.fragment_video_detail.*

/**
 * 创建人: liguangxi
 * 日期: 2019/5/3
 * 邮箱: 15679158128@163.com
 * 版本: 1.0
 * 视频详情页面
 **/
class VideoDetailFragment : MVVMFragment<VideoViewModel>() {
    private var videoId: String? = null

    companion object {

        fun newInstance(args: Bundle): VideoDetailFragment {
            val fragment = VideoDetailFragment()
            fragment.arguments = args
            return fragment
        }

        const val extra_video_id = "video_id"
    }

    override fun createViewModel(): VideoViewModel = VideoViewModel(context)

    override fun getLayoutId(): Int = R.layout.fragment_video_detail

    override fun viewCreate(savedInstanceState: Bundle?) {
        // 设置间距
        val statusBarHeight = StatusBarUtil.getStatusBarHeight(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val dlHeaderLP = mVideoPlayer.dlHeader.layoutParams as LinearLayout.LayoutParams
            dlHeaderLP.height = statusBarHeight
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mVideoPlayerLP = mVideoPlayer.layoutParams as LinearLayout.LayoutParams
            mVideoPlayerLP.height = mVideoPlayerLP.height + statusBarHeight
        }

        mVideoPlayer.ivVideoBack.setOnClickListener { pop() }

        videoId = arguments.getString(extra_video_id)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mVideoPlayer.videoId = videoId

        mVideoPlayer.setUp("", JCVideoPlayer.SCREEN_WINDOW_NORMAL, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        JCVideoPlayer.releaseAllVideos()
    }
}