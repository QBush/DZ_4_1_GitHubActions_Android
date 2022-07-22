package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.PostRepository.Companion.NEW_POST_ID
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    private val posts // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }

    // берем данные из БД и преобразуем каждую из строк в пост и возвращаем массив из них
    override val data = dao.getAll().map{ entities ->
        entities.map { it.toModel() }
    }

    override fun save(post: Post) {
       dao.save(post.toEntity())
    }

    override fun like(postId: Long) {
        dao.likeById(postId)
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
    }


    override fun share(postId: Long) {
        dao.shareById(postId)
    }

}
