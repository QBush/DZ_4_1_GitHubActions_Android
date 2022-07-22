package ru.netology.nmedia.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.UI.FeedFragmentDirections.Companion.toPostContentFragment
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.findPostById
import ru.netology.nmedia.thousandKChanger
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.properties.Delegates


class PostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        // TODO 4 Получаем id поста из FeedFragment.
        val currentId = args.initialPostID

        val viewHolder = PostAdapter.ViewHolder(binding.postLayout, viewModel)
        viewModel.data.observe(viewLifecycleOwner) {
            findPostById(currentId, it)?.let { it1 -> viewHolder.bind(it1) }
                ?: findNavController().popBackStack()
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
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

        viewModel.navigateToPostContentFromFeedFragment.observe(viewLifecycleOwner) {
            val direction = PostFragmentDirections.postFragmentToPostContentFragment(it)
            findNavController().navigate(direction)
        }

        viewModel.playVideoEventFromExternalActivity.observe(viewLifecycleOwner) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }

// пустой, чтобы отрабатывал, но не запускался (тогда не будет работать, что нам и нужно)
        viewModel.navigateToPostFragment.observe(viewLifecycleOwner) {}

    }.root
}




