package ru.netology.nmedia.adapter

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
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


    inner class ViewHolder(
        private val binding: PostListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.likes.setOnClickListener { interactionListener.onLikeClick(post) }
            binding.share.setOnClickListener { interactionListener.onShareClick(post) }
        }
        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            interactionListener.onRemoveClick(post)
                            true
                        }
                        R.id.edit -> {
                            interactionListener.onEditClick(post)

                            true
                        }
                        else -> false
                    }
                }
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                date.text = post.published
                postText.text = post.content
                likes.setImageResource(getLikeIconResId(post.likedByMe))
                likesCount.text = thousandKChanger(post.likeCount)
                sharedCount.text = thousandKChanger(post.shareCount)
                options.setOnClickListener { popupMenu.show() }
            }
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