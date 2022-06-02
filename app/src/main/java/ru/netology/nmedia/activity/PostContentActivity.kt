package ru.netology.nmedia.activity
// Активити для редактирования и создания поста

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.netology.nmedia.PostEditableContent
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        var text = intent.getStringExtra(CONTENT_KEY)
        lateinit var url : String
        binding.postTextAndUrlActivity.edit.setText(text)

        binding.postTextAndUrlActivity.edit.requestFocus()
        binding.ok.setOnClickListener {
            if (binding.postTextAndUrlActivity.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                text = binding.postTextAndUrlActivity.edit.text.toString()
                url = binding.postTextAndUrlActivity.videoUrl.text.toString()
                intent.putExtra(CONTENT_KEY, text)
                intent.putExtra(URL_KEY, url)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

//    object ResultContract : ActivityResultContract<String?, String?>(){
//        override fun createIntent(context: Context, input: String?): Intent =
//            Intent(context, PostContentActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)
//
//
//        override fun parseResult(resultCode: Int, intent: Intent?): String? =
//            if (resultCode == Activity.RESULT_OK) {
//                intent?.getStringExtra(RESULT_KEY)
//            } else null
//    }

    object ResultContractWithUrl :
        ActivityResultContract<PostEditableContent?, PostEditableContent?>() {
        override fun createIntent(context: Context, input: PostEditableContent?): Intent {
            val intent = Intent(context, PostContentActivity::class.java)
            intent.putExtra(CONTENT_KEY, input?.content)
            intent.putExtra(URL_KEY, input?.videoUrl)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): PostEditableContent? {
            if (resultCode == Activity.RESULT_OK) {
                return PostEditableContent(
                    intent?.getStringExtra(CONTENT_KEY),
                    intent?.getStringExtra(URL_KEY)
                )
            } else return null
        }
    }

    private companion object {
        private const val CONTENT_KEY = "PostContent"
        private const val URL_KEY = "PostUrl"

    }

}