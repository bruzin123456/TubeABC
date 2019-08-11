package com.ufabc.tubeabc.model.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ufabc.tubeabc.model.data.Video

object VideoDAO {

    val videosLiveData: LiveData<List<Video>> = MutableLiveData()

    fun postVideos(videos: List<Video>){
        (videosLiveData as MutableLiveData).postValue(videos)
    }

    fun getVideo(i : Int): Video?{
        return videosLiveData.value?.get(i);
    }

    fun getVideoById(id: Long): Video?{
        val videos = videosLiveData.value!!
        for(v : Video in videos){
            if(v.id.equals(id)){
                return v
            }
        }
        return null
    }

    fun videoCount(): Int{
        return videosLiveData.value!!.size
    }

}