package com.example.nagwatask.data.mian

import android.content.Context
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

    private val fileMapperList = mutableListOf<FileMapper>()

    fun setData(newList: List<FileMapper>) {
        fileMapperList.clear()
        fileMapperList.addAll(newList)
        notifyDataSetChanged()
//        val diffCallback = RatingDiffCallback(fileMapperList, newList)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        fileMapperList.clear()
//        fileMapperList.addAll(newList)
//        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(downloadState: FileMapper.DownloadState, position: Int, percent: Double, file: File?) {
        fileMapperList[position].downloadState = downloadState
        fileMapperList[position].progressDownload = percent.toInt()
        fileMapperList[position].file = file
        notifyItemChanged(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
        FileViewHolder(FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) =
        holder.bind(fileMapperList[position])

    override fun getItemCount(): Int = fileMapperList.size

    inner class FileViewHolder(private val itemBinding: FileItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: FileMapper) {
            itemBinding.downloadButton.setOnClickListener { clickOnButton(item) }
            changeButton(item)
            itemBinding.fileNameTextView.text = item.name
            itemBinding.fileTypeTextView.text = item.type
            itemBinding.progressContentLoadingFileDownload.progress = item.progressDownload
        }

        private fun clickOnButton(item: FileMapper) {
            if (item.downloadState == FileMapper.DownloadState.NotStartedYet) {
                item.let { model -> clickListener.onClickListener(model, adapterPosition) }
                item.downloadState = FileMapper.DownloadState.Downloading
//                downloading()
                notifyItemChanged(adapterPosition)
            }
        }

        private fun changeButton(item: FileMapper) {
            when (item.downloadState) {
                FileMapper.DownloadState.Downloaded -> downloaded()
                FileMapper.DownloadState.Downloading -> downloading()
                else -> notYetStarted()
            }
        }

        private fun downloading() {
            itemBinding.downloadButton.visibility = View.GONE
            itemBinding.progressContentLoadingFileDownload.visibility = View.VISIBLE
            itemBinding.progressContentLoadingFileDownload.show()
        }

        private fun downloaded() {
            itemBinding.downloadButton.text = context.getString(R.string.open)
            itemBinding.downloadButton.icon =
                AppCompatResources.getDrawable(context, R.drawable.check_icon)
            itemBinding.progressContentLoadingFileDownload.hide()
        }

        private fun notYetStarted() {
            itemBinding.downloadButton.text = context.getString(R.string.download)
            itemBinding.downloadButton.icon =
                AppCompatResources.getDrawable(context, R.drawable.download_icon)
            itemBinding.progressContentLoadingFileDownload.hide()
        }
    }


//    class RatingDiffCallback(
//        private val oldList: List<FileMapper>,
//        private val newList: List<FileMapper>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int = oldList.size
//
//        override fun getNewListSize(): Int = newList.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition].id === newList[newItemPosition].id
//        }
//
//        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
//            return oldList[oldPosition] == newList[newPosition]
//        }
//
//        @Nullable
//        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
//            return super.getChangePayload(oldPosition, newPosition)
//        }
//    }


}