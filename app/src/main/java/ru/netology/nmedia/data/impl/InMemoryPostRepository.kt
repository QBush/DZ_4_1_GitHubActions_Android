package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData<Post>(
        Post(
            id = 1,
            author = "Netology",
            content = "Привет! Это Нав... Это Нетология!",
            published = "28.04.2022"
        )
    )

    override val likeCount = MutableLiveData<Int>(0)
    override val shareCount = MutableLiveData<Int>(0)


    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "value should not be null"
        }

        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe
        )

        if (likedPost.likedByMe) {
            likeCount.value = likeCount.value?.plus(1)
        } else {
            likeCount.value = likeCount.value?.minus(1)
        }
        data.value = likedPost
    }

    override fun share() {
        shareCount.value = shareCount.value?.plus(1)
    }
}

