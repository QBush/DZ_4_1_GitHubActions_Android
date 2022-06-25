package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class SharedPrefsPostRepository( // через shared Preferenses
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextID: Long by Delegates.observable(
        // это все, чтобы сохранялся ID постов при перезапуске в префах
        // после этой операции в префах по другому ключу сохранилось новое значение ID
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }


    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "id"
    }

    private var posts // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }
        set(value) { // обновлеям префс и data при кждом изменении в постах
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init { // читаем из Преференса взятого
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun like(postID: Long) {
        posts = posts.map {
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
        posts = posts.map {
            if (it.id == postID) {
                it.copy(shareCount = it.shareCount + 1)
            } else it
        }
    }

    override fun delete(postID: Long) {
        posts = posts.filter { it.id != postID }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextID)) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

}

