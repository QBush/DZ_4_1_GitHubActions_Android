package ru.netology.nmedia.adapter

import ru.netology.nmedia.Post

interface PostInteractionListener {
    fun onLikeClick(post : Post)
    fun onShareClick(post : Post)
    fun onRemoveClick(post : Post)
    fun onEditClick(post : Post)

}