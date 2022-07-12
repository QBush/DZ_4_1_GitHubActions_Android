package ru.netology.nmedia.UI
// Активити для редактирования и создания поста

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.PostEditableContent
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<PostContentFragmentArgs>()

//    private val initialText
//        get() = requireArguments().getString(INITIAL_TEXT_KEY)
//    private val initialUrl
//        get() = requireArguments().getString(INITIAL_URL_KEY)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        binding.postTextAndUrlActivity.edit.requestFocus()
        binding.postTextAndUrlActivity.edit.setText(args.initialContent?.content)
        binding.postTextAndUrlActivity.videoUrl.setText(args.initialContent?.videoUrl)

        binding.ok.setOnClickListener {
            if (!binding.postTextAndUrlActivity.edit.text.isNullOrBlank()) {
                val text = binding.postTextAndUrlActivity.edit.text.toString()
                val url = binding.postTextAndUrlActivity.videoUrl.text.toString()
                viewModel.onSaveButtonClick(text, url)
//                val resultBundle = Bundle(2)
//                resultBundle.putString(CONTENT_KEY, text)
//                resultBundle.putString(URL_KEY, url)
//                setFragmentResult(REQUEST_KEY, resultBundle)
            }
            findNavController().popBackStack() // уходим назад с этого фрагмента

        }
    }.root

    companion object {
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"

//        private const val INITIAL_TEXT_KEY = "PostInitialText"
//        private const val INITIAL_URL_KEY = "PostInitialUrl"


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
