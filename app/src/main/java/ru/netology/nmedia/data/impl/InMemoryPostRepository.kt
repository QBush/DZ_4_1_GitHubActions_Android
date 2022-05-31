package ru.netology.nmedia.data.impl

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

    private var nextID = GENERATED_POST_AMOUNT.toLong()

    private companion object {
        const val GENERATED_POST_AMOUNT = 1000
    }

    private val posts // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }

    override val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Random content $index",
                published = "04.05.2022"
            )
        }
    )

    override fun like(postID: Long) {
        data.value = posts.map {
            if (it.id == postID) {
                if (it.likedByMe) {
                    it.likeCount--
                } else {
                    it.likeCount++
                }
                it.copy(likedByMe = !it.likedByMe)
            } else it
        }
    }

    override fun share(postID: Long) {
            data.value = posts.map {
                if (it.id == postID) {
                    it.copy(shareCount = it.shareCount+1)
            } else it
        }
    }

    override fun delete(postID: Long) {
        data.value = posts.filter { it.id != postID }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextID)) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    override fun playVideo(post: Post) {
        Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))

    }

}

