package com.example.nagwatask.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.nagwatask.R
import com.example.nagwatask.data.mian.FileAdapter
import com.example.nagwatask.databinding.ActivityMainBinding
import com.example.nagwatask.interfaces.ClickListener
import com.example.nagwatask.models.LoadingState
import com.example.nagwatask.models.SuccessState
import com.example.nagwatask.models.file.FileMapper
import com.example.nagwatask.ui.MyApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ClickListener<FileMapper> {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var fileAdapter: FileAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        MyApplication().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromApi()
        initAdapterWithRecycler()
    }

    private fun initAdapterWithRecycler() {
        fileAdapter = FileAdapter(applicationContext, this)
        binding.fileRecycler.adapter = fileAdapter
    }

    private fun getDataFromApi() {
        mainViewModel.callApi()
        mainViewModel.getFileApi().observe(this, Observer {
            if(it is SuccessState) setDataToAdapter(it.data!!)
            else if(it is LoadingState) showShimmerEffect()
        })
    }

    private fun showShimmerEffect() {
        binding.shimmerFileLayout.startShimmer()
        binding.shimmerFileLayout.visibility = View.VISIBLE
        binding.fileRecycler.visibility = View.GONE
    }

    private fun setDataToAdapter(data: List<FileMapper>) {
        binding.shimmerFileLayout.stopShimmer()
        binding.shimmerFileLayout.visibility = View.GONE
        binding.fileRecycler.visibility = View.VISIBLE
        fileAdapter.setData(data)
    }

    override fun onClickListener(model: FileMapper) {

    }
}