package ru.netology.nmedia.UI
// Активити для редактирования и создания поста

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.PostEditableContent
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentFragment : Fragment() {

    private var initialText = requireArguments().getString(INITIAL_TEXT_KEY)
    private var initialUrl = requireArguments().getString(INITIAL_URL_KEY)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentActivityBinding.inflate(layoutInflater, container, false).also { binding ->

// TODO по лекции забрать из arguments данные и присвоить их стартовым полям.


        binding.postTextAndUrlActivity.edit.requestFocus()
        binding.postTextAndUrlActivity.edit.setText(initialText)
        binding.postTextAndUrlActivity.videoUrl.setText(initialUrl)

        binding.ok.setOnClickListener {
            if (!binding.postTextAndUrlActivity.edit.text.isNullOrBlank()) {
                val text = binding.postTextAndUrlActivity.edit.text.toString()
                val url = binding.postTextAndUrlActivity.videoUrl.text.toString()
                val resultBundle = Bundle(2)
                resultBundle.putString(CONTENT_KEY, text)
                resultBundle.putString(URL_KEY, url)
                setFragmentResult(REQUEST_KEY, resultBundle)
            }
            parentFragmentManager.popBackStack() // уходим назад с этого фрагмента

        }
    }.root

    companion object {
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"
        private const val INITIAL_TEXT_KEY = "PostInitialText"
        private const val INITIAL_URL_KEY = "PostInitialText"


        // функция, которую мы вызываем при создании Фрагмента. Потом из аргументов достаем данные здесь же
        fun create(initialContent: PostEditableContent?) = PostContentFragment().apply {
            arguments = Bundle(2).also {
                if (initialContent == null) return@apply
                it.putString(INITIAL_TEXT_KEY, initialContent.content)
                it.putString(INITIAL_URL_KEY, initialContent.videoUrl)
            }
        }
    }
}
