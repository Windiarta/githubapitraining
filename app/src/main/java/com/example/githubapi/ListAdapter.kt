package com.example.githubapi

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.database.Data
import com.example.githubapi.database.DataRepository
import com.example.githubapi.databinding.ItemRowBinding
import java.util.*

class ListAdapter(private val User: ArrayList<ItemsItem>, private val updateViewModel: DataUpdateViewModel, application: Application, private val owner: LifecycleOwner) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val mNoteRepository = DataRepository(application)
    private val usernames: LiveData<List<String>> = mNoteRepository.getAllUsername()
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = User[position]
        Glide.with(holder.binding.root)
            .load(user.avatarUrl)
            .placeholder(R.drawable.profile_foreground)
            .error(R.drawable.profile_foreground)
            .into(holder.binding.circleImage)
        holder.binding.itemName.text = user.login

        usernames.observe(owner){
            if(it.contains(user.login)){
                holder.binding.itemFavorite.apply{
                    setImageResource(R.drawable.favorite_foreground)
                    tag = "isFavorite"
                }
            } else {
                holder.binding.itemFavorite.apply{
                    setImageResource(R.drawable.favorite_border_foreground)
                    tag = "notFavorite"
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(User[holder.adapterPosition])
        }
        holder.binding.itemFavorite.apply{
            setOnClickListener{
                if(tag.equals("isFavorite")){
                    tag = "notFavorite"
                    setImageResource(R.drawable.favorite_border_foreground)
                    updateViewModel.delete(user.login)
                } else {
                    tag = "isFavorite"
                    setImageResource(R.drawable.favorite_foreground)
                    updateViewModel.insert(Data(0, user.login, user.avatarUrl))
                }
            }
        }
    }

    override fun getItemCount(): Int = User.size

    class ListViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}