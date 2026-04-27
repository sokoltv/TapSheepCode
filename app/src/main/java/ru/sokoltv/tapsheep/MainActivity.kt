package ru.sokoltv.tapsheep

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    // НОВОЕ: Переменная для хранения количества тапов
    private var tapCount = 0
    // НОВОЕ: Константы для сохранения данных
    companion object {
        val PREFS_NAME = "SheepTapperPrefs"
        val TAP_COUNT_KEY = "TapCount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим наши элементы на экране по их ID
        val sheepImageView: ImageView = findViewById(R.id.sheepImageView)
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        // НОВОЕ: Находим TextView для счетчика
        val tapCounterTextView: TextView = findViewById(R.id.tapCounterTextView)

        setSupportActionBar(topAppBar)
        mediaPlayer = MediaPlayer.create(this, R.raw.sheep_sound)

        // НОВОЕ: Загружаем сохраненное количество тапов
        loadTapCount()
        // НОВОЕ: Устанавливаем загруженное значение в TextView
        tapCounterTextView.text = tapCount.toString()

        // Анимации (остаются без изменений)
        val pressAnimation = ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 100 }
        val releaseAnimation = ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 100 }

        sheepImageView.setOnClickListener {
            // НОВОЕ: Увеличиваем счетчик
            tapCount++
            // НОВОЕ: Обновляем текст на экране
            tapCounterTextView.text = tapCount.toString()
            // НОВОЕ: Сохраняем новое значение
            saveTapCount()

            it.startAnimation(pressAnimation)
            it.postDelayed({ it.startAnimation(releaseAnimation) }, 100)

            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(0)
            }
            mediaPlayer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTapCount()
        findViewById<TextView>(R.id.tapCounterTextView).text = tapCount.toString()
    }

    // НОВЫЙ МЕТОД: Сохранение счетчика
    private fun saveTapCount() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(TAP_COUNT_KEY, tapCount)
        editor.apply()
    }

    // НОВЫЙ МЕТОД: Загрузка счетчика
    private fun loadTapCount() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Загружаем значение. Если ничего не сохранено, по умолчанию будет 0.
        tapCount = prefs.getInt(TAP_COUNT_KEY, 0)
    }

    // Остальной код (меню, onDestroy) без изменений
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}