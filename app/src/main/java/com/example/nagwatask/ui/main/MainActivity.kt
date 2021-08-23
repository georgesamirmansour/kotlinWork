package com.example.nagwatask.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.nagwatask.R
import com.example.nagwatask.databinding.ActivityMainBinding
import com.example.nagwatask.ui.MyApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        MyApplication().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)
        getDataFromApi()

    }

    private fun getDataFromApi(){
        mainViewModel.callApi()
        mainViewModel.getFileApi().observe(this, Observer {
            Log.d(MainActivity::class.java.name, "getDataFromApi() called ${it.data}")
        })
    }
}