package ru.netology.nmedia.UI
//

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.IntentHandlerActivityBinding

class IntentHandLerActivity : AppCompatActivity() { // класс для приема внешних интентов из других программ
    //наш пример см метод sharePostContent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = IntentHandlerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent ?: return

        if (intent.action != Intent.ACTION_SEND) return

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (text.isNullOrBlank()) return

        Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE) // выводим SnackBar с действием
            .setAction(android.R.string.ok) { finish() } // по нажатии "ОК" выходим из программы
            .show()
    }
}