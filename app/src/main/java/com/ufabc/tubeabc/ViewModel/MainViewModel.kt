package com.ufabc.tubeabc.ViewModel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ufabc.tubeabc.model.data.Video
import com.ufabc.tubeabc.model.service.VideoService
import java.security.AccessControlContext

class MainViewModel : ViewModel() {


    var videoServiceBound = false
    lateinit var videoServiceBinder : VideoService.LocalBinder

    lateinit var context: Context


    val videoServiceConnetion = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            videoServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            videoServiceBound = true
            videoServiceBinder = binder as VideoService.LocalBinder
            //
            updateVideos()
        }
    }

    fun initialize(context: Context){
        this.context = context
        if(!videoServiceBound) {
            Intent(context, VideoService::class.java).apply {
                context.bindService(this, videoServiceConnetion, Context.BIND_AUTO_CREATE)
            }
        }
    }

    fun updateVideos(){
        videoServiceBinder.fetchVideos()
    }

    fun downloadVideoThumbnail(video: Video, callback: VideoService.OnThumbnailLoaded){
        videoServiceBinder.downloadThumbnail(video, callback)
    }

    override fun onCleared() {
        context.unbindService(videoServiceConnetion)
    }


}