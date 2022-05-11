package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding

internal class PostAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback) {

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

//        private lateinit var post : Post
//        init {
//            binding.likes.setOnClickListener { onLikeClicked(post) }
//            binding.share.setOnClickListener { onShareClick(post) }
//        }

        fun bind(post: Post) = with(binding) {
            authorName.text = post.author
            date.text = post.published
            postText.text = post.content
            likes.setImageResource(getLikeIconResId(post.likedByMe))
            likes.setOnClickListener { onLikeClicked(post) }
            share.setOnClickListener { onShareClick(post) }
            likesCount.text = thousandKChanger(post.likeCount)
            sharedCount.text = thousandKChanger(post.shareCount)
        }


        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24
    }

    private fun thousandKChanger(number: Long): String =
        when (number) {
            0L -> ""
            in 1..999 -> number.toString()
            in 1000..9999 -> "${String.format("%.1f", (number.toDouble() / 1000))}K"
            in 10_000..999_999 -> "${number / 1000}K"
            else -> "${number / 1_000_000}M"
        }

    // для сравнения объектов через ListAdapter
    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}