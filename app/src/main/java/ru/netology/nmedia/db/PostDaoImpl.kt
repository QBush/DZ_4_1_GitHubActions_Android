package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.Post

// здесь реализуем обновление базы данных в зависимости от вызываемых методов. Их будем вызывать в репозитории.
class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {

    override fun getAll() =
        db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAME,
            null, null, null, null,
            "${PostsTable.Column.ID.columnName} DESC"
        ).use { cursor ->
            List(cursor.count) {
                cursor.moveToNext()
                cursor.toPost()
            }
        }

    override fun removeById(id: Long) {
        // whereClause - что мы хотим обновить, массив следом - что мы ставим на знак вопроса
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun likeById(id: Long) {
        // likeCount и likedByMe – названия колонок
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
                """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                shareCount = shareCount + 1
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }


    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.VIDEO_URL.columnName, post.videoUrl)
        }
        val id = if (post.id != 0L) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else { // если у поста id = 0, значит его еще нет в таблице, значит вставляем его в таблицу и получаем id
            db.insert(PostsTable.NAME, null, values)
        }

        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAME,
            "${PostsTable.Column.ID.columnName} =?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }
}
// берет данные из строки таблицы, сохраняя их в пост
fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    videoUrl = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_URL.columnName)),
    // ниже true, если верно, false если неверно. Нужен Int для этого
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    likeCount = getLong(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    shareCount = getLong(getColumnIndexOrThrow(PostsTable.Column.SHARES.columnName)),
)
