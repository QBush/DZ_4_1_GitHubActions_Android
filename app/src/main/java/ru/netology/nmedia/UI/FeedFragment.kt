package ru.netology.nmedia.UI

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.*
import ru.netology.nmedia.R
import ru.netology.nmedia.UI.PostContentFragment.Companion.REQUEST_KEY
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setFragmentResultListener( // принимает bundle из других фрагментов
            requestKey = REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostText = bundle.getString(PostContentFragment.CONTENT_KEY) ?: return@setFragmentResultListener
            val newPostURL = bundle.getString(PostContentFragment.URL_KEY)
            viewModel.onSaveButtonClick(newPostText, newPostURL)
        }

        // возвращаемся к старому фрагменту после изменения данных
        viewModel.navigateToPostContentScreenEventWithUrl.observe(this) {
            parentFragmentManager.commit {
                val fragment = PostContentFragment.create(it)
                replace(R.id.fragmentContainer, fragment)
                addToBackStack(null) //возвращаемся обратно
            }
        }

        viewModel.playVideoEventFromExternalActivity.observe(this) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ActivityMainBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = PostAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it) // метод вызывает обновление адаптера
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }
    }.root

    companion object {
        const val TEG = "FeedFragment"
    }
}



