package com.ufabc.tubeabc.view.activity

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ufabc.tubeabc.R
import com.ufabc.tubeabc.ViewModel.MainViewModel
import com.ufabc.tubeabc.model.data.Video
import com.ufabc.tubeabc.model.service.VideoService
import kotlinx.android.synthetic.main.cardview_video.view.*

class VideoListAdapter(private val videos: List<Video>, private val mainViewModel: MainViewModel, private val context: Context) : RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {


    class MyViewHolder(val container: View,
                       val videoThumbnailImage: AppCompatImageView,
                       val videoTitleText: TextView,
                       val videoViewCountText: TextView,
                       val videoDateText: TextView) : RecyclerView.ViewHolder(container){
        var video : Video? = null
    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VideoListAdapter.MyViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.cardview_video, parent, false)
        val videoThumbnailImage = container.iv_video_thumbnail
        val videoTitleText = container.tv_video_title
        val videoViewCountText = container.tv_video_views_count
        val videoDateText = container.tv_video_date
        return MyViewHolder(container, videoThumbnailImage, videoTitleText, videoViewCountText, videoDateText)
    }

    override fun onBindViewHolder(h: MyViewHolder, position: Int) {
        val video = videos.get(position)
        h.video = video
        h.videoTitleText.text = video.title
        h.videoViewCountText.text = video.views.toString()
        h.videoDateText.text = video.recorded
        if(video.thumbnailImage != null) {
            h.videoThumbnailImage.setImageBitmap(video.thumbnailImage)
        } else{
            mainViewModel.downloadVideoThumbnail(video, object  :VideoService.OnThumbnailLoaded{
                override fun onThumbnailLoaded(video: Video) {
                    if(Looper.myLooper() != Looper.getMainLooper()){
                        return
                    }
                    if(video != h.video)
                        return
                    h.videoThumbnailImage.setImageBitmap(video.thumbnailImage)
                }
            })
        }
        h.container.setOnClickListener {
            val intent = Intent(context, VideoDetailsActivity::class.java)
            intent.putExtra(VideoDetailsActivity.VIDEO_ID_EXTRA, video.id)

            context.startActivity(intent)

        }
    }

    override fun getItemCount() = videos.size

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.video = null
    }
}