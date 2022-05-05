package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) {
            binding.render(it)
        }

//        viewModel.likeCount.observe(this) {
//            binding.likesCount.text = thousentKChanger(it)
//        }
//
//        viewModel.shareCount.observe(this) {
//            binding.sharedCount.text = thousentKChanger(it)
//        }
//
//        binding.share.setOnClickListener {
//            viewModel.onShareClick()
//        }
    }

    private fun ActivityMainBinding.render(posts: List<Post>) {
        for (post in posts) {
            val postBinding = PostListItemBinding.inflate(
                layoutInflater, postsLeanerLayout, true
            )
//            root.addView(PostListItemBinding.root) - то же самое, если вторые 2 показателя не писать
            postBinding.render(post)
        }
    }

    private fun PostListItemBinding.render(post: Post) {
        authorName.text = post.author
        date.text = post.published
        postText.text = post.content
        likes.setImageResource(getLikeIconResId(post.likedByMe))
        likes.setOnClickListener { viewModel.onLikeClick(post) }
    }

    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24

    private fun thousentKChanger(number: Int): String =
        when (number) {
            0 -> ""
            in 1..999 -> number.toString()
            in 1000..9999 -> "${String.format("%.1f", (number.toDouble() / 1000))}K"
            in 10_000..999_999 -> "${number / 1000}K"
            else -> "${number / 1_000_000}M"
        }
}
