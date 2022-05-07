package ru.netology.nmedia.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import kotlin.properties.Delegates

internal class PostAdapter(
    private val onLikeClicked: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    var posts: List<Post> by Delegates.observable(emptyList()) {_,_,_ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    inner class ViewHolder(
        private val binding: PostListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) = with(binding) {
            authorName.text = post.author
            date.text = post.published
            postText.text = post.content
            likes.setImageResource(getLikeIconResId(post.likedByMe))
            likes.setOnClickListener { onLikeClicked(post) }
        }

        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24
    }

}