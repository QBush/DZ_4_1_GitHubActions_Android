package ru.netology.nmedia.UI

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
    }.root
}

//class PostFragment : Fragment() {
//
//    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
//    private val args by navArgs<PostFragmentArgs>()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ) = PostFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
//
//        // TODO 4 Получаем id поста из FeedFragment.
//        val currentId = args.initialPostID
//        val currentPost = viewModel.data.value?.let { findPostById(currentId, it) }
//            ?: findNavController().run {
//                popBackStack()
//                return@also
//            }
//
//        with(binding.postLayout) {
//            authorName.text = currentPost.content
//            date.text = currentPost.published
//            postText.text = currentPost.content
//            likes.isChecked = currentPost.likedByMe
//            likes.text = currentPost.likeCount.thousandKChanger()
//            share.text = currentPost.shareCount.thousandKChanger()
//            if (currentPost.videoUrl?.isNotBlank() == true) {
//                video.root.visibility = View.VISIBLE
//            }
//        }
//
//        val popupMenu by lazy {
//            PopupMenu(context, binding.postLayout.options).apply {
//                inflate(R.menu.options_post)
//                setOnMenuItemClickListener { menuItem ->
//                    when (menuItem.itemId) {
//                        R.id.remove -> {
//                            viewModel.onRemoveClick(currentPost)
//                            true
//                        }
//                        R.id.edit -> {
//                            viewModel.onEditClick(currentPost)
//                            true
//                        }
//                        else -> false
//                    }
//                }
//            }
//        }
//
//        binding.postLayout.likes.setOnClickListener { viewModel.onLikeClick(currentPost) }
//        binding.postLayout.share.setOnClickListener { viewModel.onShareClick(currentPost) }
//        binding.postLayout.options.setOnClickListener { popupMenu.show() }
//        binding.postLayout.video.background.setOnClickListener {
//            viewModel.onVideoClick(currentPost)
//        }
//
//        viewModel.data.observe(viewLifecycleOwner) {
//            findPostById(currentId, it)?.let { it1 -> bind(it1, binding) }
//                ?: findNavController().popBackStack()
//        }
//
//    }.root
//
//
//    private fun bind(post: Post, binding:PostFragmentBinding) {
//        with(binding.postLayout) {
//            authorName.text = post.author
//            date.text = post.published
//            postText.text = post.content
//            likes.isChecked = post.likedByMe
//            likes.text = post.likeCount.thousandKChanger()
//            share.text = post.shareCount.thousandKChanger()
//            if (post.videoUrl?.isNotBlank() == true) {
//                video.root.visibility = View.VISIBLE
//            }
//        }
//    }
//}



