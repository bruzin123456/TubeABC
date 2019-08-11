package com.ufabc.tubeabc.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufabc.tubeabc.R
import com.ufabc.tubeabc.ViewModel.MainViewModel
import com.ufabc.tubeabc.model.dao.VideoDAO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)

        VideoDAO.videosLiveData.observe(this, Observer {
            recyclerview.adapter = VideoListAdapter(it, viewModel, this)
        })
        viewModel.initialize(applicationContext)
    }
}
