package ru.netology.nmedia.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import kotlin.properties.Delegates

internal class PostAdapter(
    private val onLikeClicked: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


//    override fun getItemCount() = posts.size - не нужно для ListAdapter, только для RecycleView.Adapter

    inner class ViewHolder(
        private val binding: PostListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) = with(binding) {
            authorName.text = post.author
            date.text = post.published
            postText.text = post.content
            likes.setImageResource(getLikeIconResId(post.likedByMe))
            likes.setOnClickListener { onLikeClicked(post) }
//            likesCount.text = likeCount
        }



        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24
    }


    // для сравнения объектов через ListAdapter
    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}