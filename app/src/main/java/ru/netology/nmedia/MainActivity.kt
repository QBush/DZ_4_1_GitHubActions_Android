package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.post_list_item)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Netology",
            content = "Привет! Это Нав... Это Нетология!",
            published = "28.04.2022"
        )

        with(binding) {
            postListItem.authorName.text = post.author
            postListItem.date.text = post.published
            postListItem.postText.text = post.content
            if (post.likedByMe) {
                postListItem.likes.setImageResource(R.drawable.ic_liked_24)
            }
        }

        binding.postListItem.likes.setOnClickListener {
            post.likedByMe = !post.likedByMe
            val imageResId =
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            binding.postListItem.likes.setImageResource(imageResId)
        }
    }
}
//    private fun ActivityMainBinding.render(post: Post) {
//        postListItem.authorName.text = post.author
//        postListItem.date.text = post.published
//        postListItem.postText.text = post.content
//    }
//
//    private fun getLikeIconResId (liked: Boolean) =
//        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24
//
//}