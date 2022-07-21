package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC") // выбрали из таблицы posts, сортируем DESC
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET content =:content WHERE id =:id")
    fun updateContentById(id: Long, content: String)

    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query("""
        UPDATE posts SET
        likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun likeById(id: Long)


    @Query(
        """
            UPDATE posts SET
             shareCount = shareCount + +1
             WHERE id =:id
            """
    )
    fun shareById(id: Long)

    @Query("DELETE FROM posts WHERE id =:id")
    fun removeById(id: Long)

}