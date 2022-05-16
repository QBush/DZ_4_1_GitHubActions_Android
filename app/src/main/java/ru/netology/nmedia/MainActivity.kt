package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.hideKeyBoard
import ru.netology.nmedia.utils.showKeyBoard
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

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClick(content)
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            val content = currentPost?.content
            binding.contentEditText.setText(content)
            if (content != null) {
                binding.contentEditText.requestFocus()
                //TODO область видимости кнопок
                binding.contentEditText.showKeyBoard()
            } else {
                binding.contentEditText.clearFocus()
                binding.contentEditText.hideKeyBoard()
            }
        }
    }
}



