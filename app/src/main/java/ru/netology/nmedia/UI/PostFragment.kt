package ru.netology.nmedia.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.viewModel.CurrentPostViewModel


// TODO Репозиторий нужно сделать объектом
// TODO

class PostFragment : Fragment() {

    private val viewModel by viewModels<CurrentPostViewModel>()
    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostListItemBinding.inflate(layoutInflater, container, false).also { binding ->

        // TODO 4 Получаем id поста из FeedFragment.
        val curentId = args.initialPostID
        println("4")
        // TODO на следующей строчке вылетает как "петля"
        val currentPost = viewModel.data.value?.firstOrNull {
            println("5")
            it.id == curentId
        }


        if (currentPost != null) {
            with(binding) {
                authorName.text = currentPost.author
                date.text = currentPost.published
                postText.text = currentPost.content
                likes.isChecked = currentPost.likedByMe
                likes.text = thousandKChanger(currentPost.likeCount)
                share.text = thousandKChanger(currentPost.shareCount)
                if (currentPost.videoUrl?.isNotBlank() == true) {
                    video.root.visibility = View.VISIBLE
                }
            }

            val popupMenu by lazy {
                PopupMenu(context, binding.options).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.onRemoveClick(currentPost)
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClick(currentPost)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

            binding.likes.setOnClickListener { viewModel.onLikeClick(currentPost) }
            binding.share.setOnClickListener { viewModel.onShareClick(currentPost) }
            binding.options.setOnClickListener { popupMenu.show() }
            binding.video.background.setOnClickListener {
                viewModel.onVideoClick(currentPost)
            }
        }
    }.root

    private fun thousandKChanger(number: Long): String =
        when (number) {
            0L -> ""
            in 1..999 -> number.toString()
            in 1000..9999 -> "${String.format("%.1f", (number.toDouble() / 1000))}K"
            in 10_000..999_999 -> "${number / 1000}K"
            else -> "${number / 1_000_000}M"
        }

}

