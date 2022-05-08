package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

    private val posts // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Random content $index",
                published = "04.05.2022"
            )
        }
    )

    override val likeCount = MutableLiveData<Int>(10)
    override val shareCount = MutableLiveData<Int>(0)

    override fun like(postID: Long) {
        data.value = posts.map {
            if (it.id == postID) {
                if (it.likedByMe) {
                    likeCount.value = likeCount.value?.minus(1)
                } else {
                    likeCount.value = likeCount.value?.plus(1)
                }
                it.copy(likedByMe = !it.likedByMe)
            } else it
        }
    }


    override fun share() {
        shareCount.value = shareCount.value?.plus(1)
    }
}

