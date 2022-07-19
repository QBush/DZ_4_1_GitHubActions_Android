package ru.netology.nmedia.UI
// Активити для редактирования и создания поста

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.PostEditableContent
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<PostContentFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val previuosContent = context?.getSharedPreferences(
            "previousNewPostContent", Context.MODE_PRIVATE
        )

        binding.postTextAndUrlActivity.edit.requestFocus()
        binding.postTextAndUrlActivity.edit.setText(args.initialContent?.content)
        binding.postTextAndUrlActivity.videoUrl.setText(args.initialContent?.videoUrl)
        // Ниже: если null, то мы сюда пришли не из редактирования, значит читаем сохраненный из преференс
        if (args.initialContent?.content == null) {
            val content = previuosContent?.getString(SAVED_POST_KEY, null)
            val previousNewPostContent: PostEditableContent? = if (content != null) {
                Json.decodeFromString(content)
            } else null
            binding.postTextAndUrlActivity.edit.setText(previousNewPostContent?.content)
            binding.postTextAndUrlActivity.videoUrl.setText(previousNewPostContent?.videoUrl)
        }

        //TODO не проходит код внутри edit по кнопке "назад"
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentText = binding.postTextAndUrlActivity.edit.text.toString()
            val curerntUrl = binding.postTextAndUrlActivity.videoUrl.text.toString()
            previuosContent?.edit {
                    val currentPost =
                        Json.encodeToString(PostEditableContent(currentText, curerntUrl))
                    putString(SAVED_POST_KEY, currentPost)
            }
//            requireActivity().onBackPressed()
            findNavController().popBackStack()
        }

        binding.ok.setOnClickListener {
            if (!binding.postTextAndUrlActivity.edit.text.isNullOrBlank()) {
                val text = binding.postTextAndUrlActivity.edit.text.toString()
                val url = binding.postTextAndUrlActivity.videoUrl.text.toString()
                viewModel.onSaveButtonClick(text, url)
            }
            //в этот момент очищать prefs, чтобы при создании нового поста не брать опять данные из старого
            previuosContent?.edit()?.clear()?.commit()
            findNavController().popBackStack() // уходим назад с этого фрагмента

        }
    }.root

    companion object {
        // для получения из фрагмента контента при редактировании
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"

        // Для преференс при нажатии назад
        private const val SAVED_TEXT_KEY = "PostInitialText"
        private const val SAVED_URL_KEY = "PostInitialUrl"
        private const val SAVED_POST_KEY = "PostInitialPost"


        // функция, которую мы вызываем для создания Фрагмента. Потом из аргументов достаем данные (см onCreateView)
//        fun create(initialContent: PostEditableContent?) = PostContentFragment().apply {
//            arguments = createBundle(initialContent)
//        }
//
//        fun createBundle(initialContent: PostEditableContent?) = Bundle(2).apply {
//            putString(INITIAL_TEXT_KEY, initialContent?.content)
//            putString(INITIAL_URL_KEY, initialContent?.videoUrl)
//        }
    }
}
