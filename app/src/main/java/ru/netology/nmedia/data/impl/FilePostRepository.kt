package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class FilePostRepository( // через Буфферизованные потоки
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val typeID = TypeToken.getParameterized(Long::class.java).type

    private companion object {
        const val NEXT_ID_PREFS_KEY = "id"
        const val FILE_NAME = "posts.json"
        const val FILE_NAME2 = "postsID.json"
    }


    private var nextID: Long
    init {
        val idFile = application.filesDir.resolve(FILE_NAME2)
        val id: Long = if (idFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME2)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, typeID)
            }
        } else 0L
        nextID = id
    }

    private fun syncID(currentId: Long) {
        application.openFileOutput(FILE_NAME2, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(currentId))
        }
    }

    private var posts // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }
        set(value) { // запись  в поток при каждом обновлении списка постов
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }

            data.value = value
        }

    override val data: MutableLiveData<List<Post>>
    init { // читаем с потока при старте
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
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
        syncID(nextID)
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }
}




