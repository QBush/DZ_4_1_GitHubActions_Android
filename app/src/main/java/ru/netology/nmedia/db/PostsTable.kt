package ru.netology.nmedia.db

object PostsTable {
    const val NAME = "posts"

    // Создаем таблицу
    val DDL = """
        CREATE TABLE $NAME (
        ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Column.AUTHOR.columnName} TEXT NOT NULL,
        ${Column.CONTENT.columnName}  TEXT NOT NULL,
        ${Column.PUBLISHED.columnName} TEXT NOT NULL,
        ${Column.VIDEO_URL.columnName} TEXT NOT NULL,
        ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0
        ${Column.LIKES.columnName} INTEGER NOT NULL DEFAULT 0,
        ${Column.SHARES.columnName} INTEGER NOT NULL DEFAULT 0
        );
        """.trimIndent() // убирает отступы

    // Фиксируем имена всех колонок в массиве
    val ALL_COLUMNS_NAME = Column.values().map {
        it.columnName
    }.toTypedArray()

    // Создаем класс, содержащий все виды колонок
    enum class Column (val columnName : String) {
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        VIDEO_URL("videoUrl"),
        LIKED_BY_ME("likedByMe"),
        LIKES("likeCount"),
        SHARES("shareCount")
    }
}