package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.Post

// берет данные из строки таблицы, сохраняя их в пост
internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    videoUrl = videoUrl,
    // ниже true, если верно, false если неверно. Нужен Int для этого
    likedByMe = likedByMe,
    likeCount = likeCount,
    shareCount = shareCount,
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    videoUrl = videoUrl,
    // ниже true, если верно, false если неверно. Нужен Int для этого
    likedByMe = likedByMe,
    likeCount = likeCount,
    shareCount = shareCount,
)
