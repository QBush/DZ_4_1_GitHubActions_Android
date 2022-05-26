package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.R
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

        binding.cancelEdit.setOnClickListener { // когда нажимем на крестик
            binding.cancelEdit.visibility = View.GONE
            binding.contentEditText.hideKeyBoard()
            binding.contentEditText.setText("")
            binding.contentEditText.clearFocus()
            binding.saveButton.setImageResource(R.drawable.ic_save_48)
        }

//        binding.contentEditText.observe(this) {
//            if (binding.contentEditText.text.toString().trim().isEmpty()) {
//                binding.editGroup.visibility = View.GONE
//            } else {
//                binding.editGroup.visibility = View.VISIBLE
//            }
//        }

        viewModel.currentPost.observe(this) { currentPost ->
            val content = currentPost?.content
            binding.contentEditText.setText(content)
            if (content != null) { // когда нажали редактировать, текущий пост принял значение из поста
                binding.contentEditText.requestFocus()
                binding.editGroup.visibility = View.VISIBLE
                binding.previousTextView.text = content
                binding.contentEditText.showKeyBoard()
                binding.saveButton.setImageResource(R.drawable.ic_save_48_blue)
            } else { // когда нажали сохранить, текущий пост обнулился
                binding.contentEditText.clearFocus()
                binding.contentEditText.hideKeyBoard()
                binding.editGroup.visibility = View.GONE
                binding.saveButton.setImageResource(R.drawable.ic_save_48)
            }
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
            startActivity(shareIntent)
        }


    }
}



