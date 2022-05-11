package ru.netology.nmedia.adapter

import ru.netology.nmedia.Post

interface PostInteractionListener {
    fun onLikeClicked(post : Post)
    fun onShareClick(post : Post)
}