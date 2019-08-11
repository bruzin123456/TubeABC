package com.ufabc.tubeabc.model.data

import android.graphics.Bitmap
import java.io.Serializable

class Video: Serializable{

    var id: Long = -1
    var title: String = ""
    var event: String = ""
    var duration: String = ""
    var recorded: String = ""
    var speaker: String = ""
    var views: Long = -1
    var likes: Long = -1
    var dislikes: Long = -1
    var source: String = ""
    var videoStreamUrl = ""
    var thumbnailImage:  Bitmap? = null


}


