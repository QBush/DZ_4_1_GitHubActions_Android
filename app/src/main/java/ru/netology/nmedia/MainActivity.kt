package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private var likeCount = 0
    private var shareCount = 0
    private var viewCount = 0

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) {
            binding.render(it)
        }

        binding.likes.setOnClickListener {
            viewModel.onLikeClick()
        }

        binding.share.setOnClickListener {
            binding.sharedCount.text = thousentKChanger(++shareCount)
        }
    }
}

private fun thousentKChanger(number: Int): String =
    when (number) {
        0 -> ""
        in 1..999 -> number.toString()
        in 1000..9999 -> "${String.format("%.1f", (number.toDouble() / 1000))}K"
        in 10_000..999_999 -> "${number / 1000}K"
        else -> "${number / 1_000_000}M"
    }

private fun PostListItemBinding.render(post: Post) {
    authorName.text = post.author
    date.text = post.published
    postText.text = post.content
    likes.setImageResource(getLikeIconResId(post.likedByMe))


}

private fun getLikeIconResId(liked: Boolean) =
    if (liked) {
//        binding.likesCount.text = thousentKChanger(++likeCount)
        R.drawable.ic_liked_24
    } else {
//        binding.likesCount.text = thousentKChanger(--likeCount)
        R.drawable.ic_like_24
    }


