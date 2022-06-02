package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent) // отдаем неявный интент наружу
        }

        val postContentActivityLauncherWithUrl = registerForActivityResult(
            PostContentActivity.ResultContractWithUrl
        ) { postContent -> // вызывается после parseResult из контракта
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClick(postContent.content!!, postContent.videoUrl)
        }

        viewModel.navigateToPostContentScreenEventWithUrl.observe(this) {
            postContentActivityLauncherWithUrl.launch(it)
        }

        viewModel.playVideoEventFromExternalActivity.observe(this) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }
}



