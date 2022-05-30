package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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

//        binding.cancelEdit.setOnClickListener { // когда нажимем на крестик
//            binding.cancelEdit.visibility = View.GONE
//            binding.contentEditText.hideKeyBoard()
//            binding.contentEditText.setText("")
//            binding.contentEditText.clearFocus()
//            binding.saveButton.setImageResource(R.drawable.ic_save_48)
//        }

//        binding.contentEditText.observe(this) {
//            if (binding.contentEditText.text.toString().trim().isEmpty()) {
//                binding.editGroup.visibility = View.GONE
//            } else {
//                binding.editGroup.visibility = View.VISIBLE
//            }
//        }

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

//        val postContentActivityLauncher = registerForActivityResult(
//            PostContentActivity.ResultContract
//        ) {postContent -> // вызывается после parseResult из контракта
//            postContent ?: return@registerForActivityResult
//            viewModel.onSaveButtonClick(postContent)
//        }
//
//        viewModel.navigateToPostContentScreenEvent.observe(this) {
//            if (it.isNullOrBlank()) postContentActivityLauncher.launch("")
//            postContentActivityLauncher.launch(it)
//        }

        val postContentActivityLauncherWithUrl = registerForActivityResult(
            PostContentActivity.ResultContractWithUrl
        ) {postContent -> // вызывается после parseResult из контракта
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClick(postContent.content!!)
        }

        viewModel.navigateToPostContentScreenEventWithUrl.observe(this) {
            postContentActivityLauncherWithUrl.launch(it)
        }
    }
}



