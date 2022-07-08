package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostEditableContent
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository
import ru.netology.nmedia.utils.SingleLiveEvent

class CurrentPostViewModel(
    application: Application
) : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEventWithUrl = SingleLiveEvent<PostEditableContent?>()
    val playVideoEventFromExternalActivity = SingleLiveEvent<String?>()


    private val currentPost = MutableLiveData<Post?>(null)

    //region MenuInteractionListener

    override fun onEditClick(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEventWithUrl.value =
            PostEditableContent(post.content, post.videoUrl)
    }

    //endregion MenuInteractionListener

    //region PostInteractionListener

    override fun onLikeClick(post: Post) = repository.like(post.id)
    override fun onShareClick(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onRemoveClick(post: Post) = repository.delete(post.id)

    override fun onVideoClick(post: Post) {
        playVideoEventFromExternalActivity.value = post.videoUrl
    }


    override fun onContentClick(post: Post) { }


    // endregion PostInteractionListener
}