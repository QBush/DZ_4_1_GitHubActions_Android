package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel::onLikeClick, viewModel::onShareClick)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it)
        }

//        viewModel.likeCount.observe(this) {
//            adapter.submitList(it)
//        }
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



