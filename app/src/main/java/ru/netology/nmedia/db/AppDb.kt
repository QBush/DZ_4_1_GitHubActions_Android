package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

// класс Синглтон, не вдаваться в подробности, просто вот так он создается.
class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null


        // безопасное создание синглтона
        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostsTable.DDL))
                ).also { instance = it }
            }
        }

        // создаем DbHelper
        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs
        ).writableDatabase
    }
}