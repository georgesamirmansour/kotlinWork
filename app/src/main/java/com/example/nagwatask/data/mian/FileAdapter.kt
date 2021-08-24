package com.example.nagwatask.data.mian

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nagwatask.R
import com.example.nagwatask.databinding.FileItemBinding
import com.example.nagwatask.interfaces.ClickListener
import com.example.nagwatask.models.file.FileMapper

class FileAdapter constructor(val context: Context, val clickListener: ClickListener<FileMapper>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private val fileMapperList = mutableListOf<FileMapper>()

    fun setData(newList: List<FileMapper>) {
        val diffCallback = RatingDiffCallback(fileMapperList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        fileMapperList.clear()
        fileMapperList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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

        }

        private fun clickOnButton(item: FileMapper) {
            if (!item.downloaded || !item.downloading) {
                item.let { model -> clickListener.onClickListener(model) }
                item.downloading = true
                itemBinding.downloadButton.visibility = View.GONE
                itemBinding.progressContentLoadingFileDownload.visibility = View.VISIBLE
            }
        }

        private fun changeButton(item: FileMapper) {
            when {
                item.downloaded -> {
                    itemBinding.downloadButton.text = context.getString(R.string.downloaded)
                    itemBinding.downloadButton.icon = AppCompatResources.getDrawable(context, R.drawable.check_icon)
                }
                item.downloading -> {
                    itemBinding.downloadButton.visibility = View.GONE
                    itemBinding.progressContentLoadingFileDownload.visibility = View.VISIBLE
                }
                else -> {
                    itemBinding.downloadButton.text = context.getString(R.string.download)
                    itemBinding.downloadButton.icon = AppCompatResources.getDrawable(context, R.drawable.download_icon)
                }
            }
        }
    }

    class RatingDiffCallback(private val oldList: List<FileMapper>, private val newList: List<FileMapper>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
           return oldList[oldPosition] == newList[newPosition]
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }


}