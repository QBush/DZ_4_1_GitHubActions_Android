package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar
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

//        run { // кладем в преференс при запуске приложения
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            preferences.edit {
//                putString("key", "value")
//            }
//        }
//
//        run { // читаем из референсе при запуске приложения
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            val value = preferences.getString("key","noValue") ?: return@run
//            Snackbar.make(binding.root, value, Snackbar.LENGTH_INDEFINITE).show()
//        }

        val adapter = PostAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it) // метод вызывает обновление адаптера
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
            postContentActivityLauncherWithUrl.launch(it) // it - входящий в параметре контракта
        }

        viewModel.playVideoEventFromExternalActivity.observe(this) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }
}



