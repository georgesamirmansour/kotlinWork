package com.example.nagwatask.data.mian

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.nagwatask.R
import com.example.nagwatask.databinding.FileItemBinding
import com.example.nagwatask.interfaces.ClickListener
import com.example.nagwatask.models.file.FileMapper
import java.io.File

class FileAdapter constructor(val context: Context, val clickListener: ClickListener<FileMapper>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private var fileMapperList: List<FileMapper>? = null
    private var downloading: Boolean = false
    fun setData(newList: List<FileMapper>) {
        fileMapperList = newList
        notifyDataSetChanged()
    }

    fun updateItem(
        downloadState: FileMapper.DownloadState,
        position: Int,
        percent: Double,
        file: File?
    ) {
        fileMapperList!![position].downloadState = downloadState
        fileMapperList!![position].progressDownload = percent.toInt()
        fileMapperList!![position].file = file
        Log.d(
            FileAdapter::class.java.name,
            "updateItem() called with: downloadState = $downloadState, position = $position, percent = $percent, file = $file"
        )
        if (downloadState == FileMapper.DownloadState.Downloaded || downloadState == FileMapper.DownloadState.NotStartedYet)
            downloading = false;
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return fileMapperList!![position].id!!.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
        FileViewHolder(FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) =
        holder.bind(fileMapperList!![position])

    override fun getItemCount(): Int {
        return if (fileMapperList == null) 0 else fileMapperList!!.size
    }

    inner class FileViewHolder(private val itemBinding: FileItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: FileMapper) {
            itemBinding.downloadButton.setOnClickListener { clickOnButton(item) }
            changeButton(item)
            itemBinding.fileNameTextView.text = item.name
            itemBinding.fileTypeTextView.text = item.type
//            itemBinding.progressBar.progress = item.progressDownload
        }

        private fun clickOnButton(item: FileMapper) {
            if (item.downloadState == FileMapper.DownloadState.NotStartedYet && !downloading) {
                item.let { model -> clickListener.onClickListener(model, adapterPosition) }
                downloading = true
            } else if (item.downloadState == FileMapper.DownloadState.Downloaded)
                item.let { model -> clickListener.onClickListener(model, adapterPosition) }
        }

        private fun changeButton(item: FileMapper) {
            when (item.downloadState) {
                FileMapper.DownloadState.Downloaded -> downloaded()
                FileMapper.DownloadState.Downloading -> downloading()
                FileMapper.DownloadState.NotStartedYet -> notYetStarted()
            }
        }

        private fun downloading() {
            itemBinding.downloadButton.visibility = View.GONE
            itemBinding.progressBar.visibility = View.VISIBLE
        }

        private fun downloaded() {
            itemBinding.downloadButton.text = context.getString(R.string.open)
            itemBinding.downloadButton.icon =
                AppCompatResources.getDrawable(context, R.drawable.check_icon)
            itemBinding.progressBar.visibility = View.GONE
            itemBinding.downloadButton.visibility = View.VISIBLE
        }

        private fun notYetStarted() {
            itemBinding.downloadButton.text = context.getString(R.string.download)
            itemBinding.downloadButton.icon =
                AppCompatResources.getDrawable(context, R.drawable.download_icon)
            itemBinding.progressBar.visibility = View.GONE
            itemBinding.downloadButton.visibility = View.VISIBLE
        }
    }
}