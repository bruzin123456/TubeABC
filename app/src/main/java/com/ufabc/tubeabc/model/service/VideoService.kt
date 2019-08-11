package com.ufabc.tubeabc.model.service

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ufabc.tubeabc.model.dao.VideoDAO
import com.ufabc.tubeabc.model.data.Video
import com.ufabc.tubeabc.model.serializer.VideoJSONSerializer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InterruptedIOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


private object VideoRestManager {
    private val TAG = "VideoService"
    private val VIDEO_SERVER_ADDRESS = "http://192.168.0.7:8099"
    private val VIDEO_LIST_ADDRESS = "$VIDEO_SERVER_ADDRESS/videos"
    private val VIDEO_STREAM_ADDRESS = "$VIDEO_SERVER_ADDRESS/video/%d"
    private val VIDEO_THUMBNAIL_ADDRESS = "$VIDEO_SERVER_ADDRESS/thumbnail/%d"
    private val VIDEO_VIEW_ADDRESS = "$VIDEO_SERVER_ADDRESS/view/%d"
    private val VIDEO_LIKE_ADDRESS = "$VIDEO_SERVER_ADDRESS/like/%d"
    private val VIDEO_DISLIKE_ADDRESS = "$VIDEO_SERVER_ADDRESS/dislike/%d"
    private val BUFFER_SIZE = 1024

    fun fetchVideos() : List<Video>{
        var responseContent = ""
        val videos = arrayListOf<Video>()
        try {
            val url = URL(VIDEO_LIST_ADDRESS)
            val conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == HttpURLConnection.HTTP_OK){
                val outputStream = ByteArrayOutputStream()
                val inputStream = conn.inputStream
                val buffer = ByteArray(BUFFER_SIZE)

                var len = inputStream.read(buffer)
                while (len > 0) {
                    outputStream.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                responseContent = String(outputStream.toByteArray())
                videos.addAll(VideoJSONSerializer(responseContent).deserializeVideos())
            }
        }
        catch (e: Exception){
            Log.e(TAG, "failed to fetch videos", e)
            return Collections.emptyList()
        }
        for(v : Video in videos){
            v.videoStreamUrl = String.format(VIDEO_STREAM_ADDRESS, v.id)
        }

        return videos
    }

    fun downloadThumbnail(video: Video) {
        try {
            val url = URL(String.format(VIDEO_THUMBNAIL_ADDRESS, video.id))
            val conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = conn.inputStream
                var bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                video.thumbnailImage = bitmap
            }
        }
            catch (e: Exception){
            Log.e(TAG, "failed to fetch thumbnail", e)
        }
    }

    fun viewVideo(video: Video) {
        try {
            val url = URL(String.format(VIDEO_VIEW_ADDRESS, video.id))
            val conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = conn.inputStream
                inputStream.close()
                video.views++
            }
        }
        catch (e: Exception){
            Log.e(TAG, "failed to increase view count", e)
        }
    }

    fun likeVideo(video: Video) {
        try {
            val url = URL(String.format(VIDEO_LIKE_ADDRESS, video.id))
            val conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = conn.inputStream
                inputStream.close()
            }
        }
        catch (e: Exception){
            Log.e(TAG, "failed to increase like count", e)
        }
    }

    fun dislikeVideo(video: Video) {
        try {
            val url = URL(String.format(VIDEO_DISLIKE_ADDRESS, video.id))
            val conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = conn.inputStream
                inputStream.close()
            }
        }
        catch (e: Exception){
            Log.e(TAG, "failed to increase dislike count", e)
        }
    }


}


class VideoService: Service() {

    interface OnThumbnailLoaded{ fun onThumbnailLoaded(video: Video)
    }

    class LocalBinder: Binder() {
        fun fetchVideos(){
            AsyncTask.execute {
                VideoDAO.postVideos(VideoRestManager.fetchVideos())
            }
        }
        fun downloadThumbnail(video: Video, callback : OnThumbnailLoaded){
            AsyncTask.execute {
                VideoRestManager.downloadThumbnail(video)
                Handler(Looper.getMainLooper()).post { callback.onThumbnailLoaded(video) }
            }
        }
        fun viewVideo(video: Video){
            AsyncTask.execute {
                VideoRestManager.viewVideo(video)
            }
        }
        fun likeVideo(video: Video){
            AsyncTask.execute {
                VideoRestManager.likeVideo(video)
            }
        }
        fun dislikeVideo(video: Video){
            AsyncTask.execute {
                VideoRestManager.dislikeVideo(video)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

}