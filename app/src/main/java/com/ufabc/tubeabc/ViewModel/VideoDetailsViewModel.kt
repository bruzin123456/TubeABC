package com.ufabc.tubeabc.ViewModel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

import androidx.lifecycle.ViewModel
import com.ufabc.tubeabc.model.data.Video
import com.ufabc.tubeabc.model.service.VideoService

class VideoDetailsViewModel : ViewModel() {

    var videoServiceBound = false
    lateinit var videoServiceBinder : VideoService.LocalBinder

    lateinit var context:Context
    lateinit var video:Video


    val videoServiceConnetion = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            videoServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            videoServiceBound = true
            videoServiceBinder = binder as VideoService.LocalBinder
            //
            viewVideo(video)
        }
    }

    fun initialize(context: Context, video: Video){
        this.context = context
        this.video = video
        if(!videoServiceBound) {
            Intent(context, VideoService::class.java).apply {
                context.bindService(this, videoServiceConnetion, Context.BIND_AUTO_CREATE)
            }
        }
    }

    fun viewVideo(video: Video){
        videoServiceBinder.viewVideo(video)
    }

    fun likeVideo(video: Video){
        videoServiceBinder.likeVideo(video)
    }

    fun dislikeVideo(video: Video){
        videoServiceBinder.dislikeVideo(video)
    }

    override fun onCleared() {
        context.unbindService(videoServiceConnetion)
    }



}