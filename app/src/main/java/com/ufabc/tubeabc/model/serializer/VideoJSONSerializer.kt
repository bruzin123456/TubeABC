package com.ufabc.tubeabc.model.serializer

import com.ufabc.tubeabc.model.data.Video
import org.json.JSONObject

class VideoJSONSerializer(jsonStr : String) {

    companion object VideoJSONContract {
        private const val VIDEO_OBJECT = "videos"
        private const val VIDEO_ARRAY = "videos"
        private const val VIDEO_ID = "id"
        private const val VIDEO_TITLE = "title"
        private const val VIDEO_EVENT = "event"
        private const val VIDEO_DURATION = "duration"
        private const val VIDEO_RECORDED = "recorded"
        private const val VIDEO_SPEAKER = "speaker"
        private const val VIDEO_VIEWS = "views"
        private const val VIDEO_LIKES = "likes"
        private const val VIDEO_DISLIKES = "dislikes"
        private const val VIDEO_SOURCE = "source"
    }

    private var root : JSONObject

    init {
        root = JSONObject(jsonStr)
    }

    fun deserializeVideos() : List<Video> {
        val videos = arrayListOf<Video>()

        val videosArray = root.getJSONArray(VIDEO_ARRAY)

        for (i in 0 until videosArray.length()) {
            val videoObj = videosArray.get(i) as JSONObject
            val video = Video()
            video.id = videoObj.getLong(VIDEO_ID)
            video.title = videoObj.getString(VIDEO_TITLE)
            video.title = videoObj.getString(VIDEO_TITLE)
            video.event = videoObj.getString(VIDEO_EVENT)
            video.duration = videoObj.getString(VIDEO_DURATION)
            video.recorded = videoObj.getString(VIDEO_RECORDED)
            video.speaker = videoObj.getString(VIDEO_SPEAKER)
            video.views = videoObj.getLong(VIDEO_VIEWS)
            video.likes = videoObj.getLong(VIDEO_LIKES)
            video.dislikes = videoObj.getLong(VIDEO_DISLIKES)
            video.source = videoObj.getString(VIDEO_SOURCE)
            videos.add(video)
        }

        return videos
    }
}