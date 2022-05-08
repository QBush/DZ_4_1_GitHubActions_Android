package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.activity.viewModels
import ru.netology.nmedia.data.impl.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel::onLikeClick)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it)
        }

        viewModel.likeCount.observe(this) {
            adapter.submitList(it)
        }
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

private fun thousentKChanger(number: Int): String =
    when (number) {
        0 -> ""
        in 1..999 -> number.toString()
        in 1000..9999 -> "${String.format("%.1f", (number.toDouble() / 1000))}K"
        in 10_000..999_999 -> "${number / 1000}K"
        else -> "${number / 1_000_000}M"
    }

