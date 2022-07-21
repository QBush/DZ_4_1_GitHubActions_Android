//package ru.netology.nmedia.data.impl
//
//import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia.Post
//import ru.netology.nmedia.data.PostRepository
//import ru.netology.nmedia.data.PostRepository.Companion.NEW_POST_ID
//import ru.netology.nmedia.db.PostDao
//
//class SQLiteRepository(
//    private val dao: PostDao
//
//) : PostRepository {
//
//    private val posts // значение data.value, проверенное на null
//        get() = checkNotNull(data.value) {
//            "value should not be null"
//        }
//
//    override val data = MutableLiveData(dao.getAll())
//
//    override fun save(post: Post) {
//        val id = post.id
//        val saved = dao.save(post)
//        // опять зачем-то проверяем новый/старый пост. Если новый:
//        data.value = if (id == NEW_POST_ID) {
//            listOf(saved) + posts
//            // если старый:
//        } else {
//            posts.map {
//                if (it.id != id) it else saved
//            }
//        }
//    }
//
//    override fun like(id: Long) {
//        dao.likeById(id)
//        data.value = posts.map {
//            if (it.id != id) it else it.copy(
//                likedByMe = !it.likedByMe,
//                likeCount = if (it.likedByMe) it.likeCount - 1 else it.likeCount + 1
//            )
//        }
//    }
//
//    override fun delete(id: Long) {
//        dao.removeById(id)
//        data.value = posts.filter {it.id != id}
//    }
//
//
//    override fun share(id: Long) {
//        dao.shareById(id)
//        data.value = posts.map {
//            if (it.id == id) {
//                it.copy(shareCount = it.shareCount+1)
//            } else it
//        }
//    }
//
//}
