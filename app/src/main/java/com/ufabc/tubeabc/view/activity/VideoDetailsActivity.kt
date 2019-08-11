package com.ufabc.tubeabc.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ufabc.tubeabc.ViewModel.MainViewModel
import com.ufabc.tubeabc.model.dao.VideoDAO
import com.ufabc.tubeabc.model.data.Video
import kotlinx.android.synthetic.main.activity_video_details.*

import android.net.Uri
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ufabc.tubeabc.R
import com.ufabc.tubeabc.ViewModel.VideoDetailsViewModel


class VideoDetailsActivity : AppCompatActivity() {

    companion object {
        const val VIDEO_ID_EXTRA = "videoExtra"
    }

    lateinit var viewModel : VideoDetailsViewModel
    lateinit var video: Video

    var videoLikedOrUnliked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)

        video = VideoDAO.getVideoById(intent.getLongExtra(VIDEO_ID_EXTRA, 0))!!
        viewModel = ViewModelProviders.of(this).get(VideoDetailsViewModel::class.java)
        viewModel.initialize(applicationContext, video)

        tv_video_title.text = video.title
        tv_video_date.text = video.recorded
        tv_video_views_count.text = video.views.toString()
        tv_video_likes_count.text = video.likes.toString()
        tv_video_dislikes_count.text = video.dislikes.toString()
        tv_video_event.text = video.event
        tv_video_speaker.text = video.speaker
        tv_video_duration.text = video.duration
        tv_video_source.text = video.source

        container_like.setOnClickListener {
            if(videoLikedOrUnliked)
                return@setOnClickListener
            videoLikedOrUnliked = true
            video.likes++
            tv_video_likes_count.text = video.likes.toString()
            viewModel.likeVideo(video)
            iv_video_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_thumbs_up_on))
        }

        container_dislike.setOnClickListener {
            if(videoLikedOrUnliked)
                return@setOnClickListener
            videoLikedOrUnliked = true
            video.dislikes++
            tv_video_dislikes_count.text = video.dislikes.toString()
            viewModel.dislikeVideo(video)
            iv_video_dislike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_thumbs_down_on))
        }

        //Setup Video
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        val videoUrl = Uri.parse(video.videoStreamUrl)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUrl)
        videoView.requestFocus()
        videoView.setOnPreparedListener {
            videoView.start()
        }
    }
}