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

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentFromFeedFragment = SingleLiveEvent<PostEditableContent?>()
    val navigateToPostFragment = SingleLiveEvent<Long>()
    val playVideoEventFromExternalActivity = SingleLiveEvent<String?>()

    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClick(content: String, videoUrl: String? = null) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content, videoUrl = videoUrl
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "me",
            content = content,
            published = "12.05.2022",
            videoUrl = videoUrl
        )
        repository.save(post)
        currentPost.value = null
    }

    //region MenuInteractionListener

    override fun onEditClick(post: Post) {
        currentPost.value = post
        navigateToPostContentFromFeedFragment.value =
            PostEditableContent(post.content, post.videoUrl)
    }

    //endregion MenuInteractionListener

    fun onAddClicked() {
        navigateToPostContentFromFeedFragment.call()
    }

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

    // TODO 2 здесь вызываем в FeedFragment реакцию
    override fun onContentClick(post: Post) {
        navigateToPostFragment.value = post.id

    }


    // endregion PostInteractionListener
}