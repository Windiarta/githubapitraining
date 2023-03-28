package com.example.githubapi.database

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.*
import com.example.githubapi.databinding.ItemRowBinding

class DataAdapter(private val viewModel: DataUpdateViewModel) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    private val listData = ArrayList<Data>()

    fun setListData(listData: List<Data>) {
        val diffCallback = DataDiffCallback(this.listData, listData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listData.clear()
        this.listData.addAll(listData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class DataViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            Glide.with(binding.root)
                .load(data.avatarUrl)
                .placeholder(R.drawable.profile_foreground)
                .error(R.drawable.profile_foreground)
                .into(binding.circleImage)
            binding.apply {
                itemName.text = data.login
                itemFavorite.setImageResource(R.drawable.favorite_foreground)
                itemView.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra("DATA", data.login)
                    it.context.startActivity(intent)
                }
                itemFavorite.setOnClickListener {
                    itemFavorite.setImageResource(R.drawable.favorite_border_foreground)
                    viewModel.delete(data)
                }
            }
        }
    }
}