package ru.netology.nmedia.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.UI.PostContentFragment.Companion.REQUEST_KEY
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

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

//        setFragmentResultListener( // принимает bundle из других фрагментов (из редактирования)
//            requestKey = REQUEST_KEY
//        ) { requestKey, bundle ->
//            if (requestKey != REQUEST_KEY) return@setFragmentResultListener
//            val newPostText = bundle.getString(PostContentFragment.CONTENT_KEY)
//                ?: return@setFragmentResultListener
//            val newPostURL = bundle.getString(PostContentFragment.URL_KEY)
//            viewModel.onSaveButtonClick(newPostText, newPostURL)
//        }

        viewModel.navigateToPostContentScreenEventWithUrl.observe(this) {
            val direction = FeedFragmentDirections.toPostContentFragment(it)
            findNavController().navigate(direction)
        }

        // TODO 3 Передаем id поста в PostFragment
        viewModel.navigateToPostFragment.observe(this) {
            println("3")
            val direction = FeedFragmentDirections.toPostFragment(it)
            findNavController().navigate(direction)
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
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

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



