package com.example.nagwatask.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.nagwatask.data.mian.FileAdapter
import com.example.nagwatask.databinding.ActivityMainBinding
import com.example.nagwatask.interfaces.ClickListener
import com.example.nagwatask.models.ErrorState
import com.example.nagwatask.models.LoadingState
import com.example.nagwatask.models.ProgressState
import com.example.nagwatask.models.SuccessState
import com.example.nagwatask.models.file.FileMapper
import com.example.nagwatask.ui.MyApplication
import javax.inject.Inject
import java.io.File
import android.widget.Toast

import android.content.ActivityNotFoundException

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log

import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.nagwatask.BuildConfig


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
        callApi()
        getDataFromApi()
        initAdapterWithRecycler()
        initRefreshListener()
    }

    private fun initRefreshListener() {
        binding.swipeToRefreshMain.setOnRefreshListener {
            callApi()
            binding.swipeToRefreshMain.isRefreshing = false
        }
    }

    private fun initAdapterWithRecycler() {
        fileAdapter = FileAdapter(applicationContext, this)
        fileAdapter.setHasStableIds(false)
        binding.fileRecycler.adapter = fileAdapter
    }

    private fun callApi() = mainViewModel.setFileRequest()

    private fun getDataFromApi() = mainViewModel.getFileApi().observe(this, Observer {
        when (it) {
            is SuccessState -> setDataToAdapter(it.data!!)
            is LoadingState -> showShimmerEffect()
            is ErrorState -> showErrorText(it.message!!)
        }
    })


    private fun showErrorText(errorText: String) {
        binding.shimmerFileLayout.stopShimmer()
        binding.shimmerFileLayout.visibility = View.GONE
        binding.fileRecycler.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.VISIBLE
//        binding.errorTextView.movementMethod = ScrollingMovementMethod()
        binding.errorTextView.text = errorText
    }

    private fun showShimmerEffect() {
        binding.shimmerFileLayout.startShimmer()
        binding.shimmerFileLayout.visibility = View.VISIBLE
        binding.fileRecycler.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
    }

    private fun setDataToAdapter(data: List<FileMapper>) {
        binding.shimmerFileLayout.stopShimmer()
        binding.shimmerFileLayout.visibility = View.GONE
        binding.fileRecycler.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
        fileAdapter.setData(data)
    }

    override fun onClickListener(model: FileMapper, position: Int) {
        if (model.downloadState == FileMapper.DownloadState.Downloaded) {

            val uri = model.file?.let {
                FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName.toString() + ".provider",
                    it,
                    it.name
                )
            }
            openFile(uri)
        }
        else downloadFile(model.url, position)
    }


    private fun openFile(uri: Uri?) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (uri.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf")
        } else if (uri.toString().contains(".3gp") || uri.toString()
                .contains(".mpg") || uri.toString().contains(".mpeg") || uri.toString()
                .contains(".mpe") || uri.toString().contains(".mp4") || uri.toString()
                .contains(".avi")
        ) {
            intent.setDataAndType(uri, "video/*")
        } else {
            intent.setDataAndType(uri, "*/*")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun downloadFile(url: String, position: Int) {
        setDownloadFile(url)
        getDownloadFile(position)
    }

    private fun getDownloadFile(position: Int) {
        mainViewModel.getDownloadFile().observe(this, Observer {
            when (it) {
                is SuccessState -> {
                    fileAdapter.updateItem(
                        FileMapper.DownloadState.Downloaded,
                        position,
                        100.0,
                        it.data!!.file!!
                    )
                }
                is ProgressState -> fileAdapter.updateItem(
                    FileMapper.DownloadState.Downloading,
                    position,
                    it.message!!.toDouble(),
                    null
                )
                is ErrorState -> {
                    fileAdapter.updateItem(
                        FileMapper.DownloadState.NotStartedYet,
                        position,
                        0.0,
                        null
                    )
                    Toast.makeText(this, "Can't download this file", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun setDownloadFile(url: String) {
        mainViewModel.setDownloadFile(url)
    }

}