package ru.sokoltv.tapsheep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Добавляем кнопку "Назад" в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Settings" // Устанавливаем заголовок экрана

        val privacyPolicyTextView: TextView = findViewById(R.id.privacyPolicyTextView)
        val appVersionTextView: TextView = findViewById(R.id.appVersionTextView)
        val resetCountTextView : TextView = findViewById(R.id.ResetCountTextView)

        // Слушатель для пункта "Политика конфиденциальности"
        privacyPolicyTextView.setOnClickListener {
            // Создаем Intent для запуска PrivacyPolicyActivity (создадим ее далее)
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        // Слушатель для пункта "Версия приложения"
        appVersionTextView.setOnClickListener {
            try {
                // Получаем версию приложения из сборочных файлов
                val versiontext = getString(R.string._ver)

                val versionName = packageManager.getPackageInfo(packageName, 0).versionName
                Toast.makeText(this, "$versiontext: $versionName", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                val versionerror = getString(R.string._verError)
                e.printStackTrace()
                Toast.makeText(this, versionerror, Toast.LENGTH_SHORT).show()
            }
        }
        // Показ диалога сброса счётчика
        resetCountTextView.setOnClickListener {
            showResetConfirmationDialog()
        }

    }
    // НОВЫЙ МЕТОД: Показывает диалог подтверждения
    private fun showResetConfirmationDialog() {
        val accept = getString(R.string._accept)
        val messageReset = getString(R.string._messageReset)
        val messagePositiveB = getString(R.string._messagePositiveB)
        val messageNegativeB = getString(R.string._messageNegativeB)
        AlertDialog.Builder(this)
            .setTitle(accept)
            .setMessage(messageReset)
            // Кнопка "Да" (положительный ответ)
            .setPositiveButton(messagePositiveB) { _, _ ->
                // Выполняем сброс
                resetTapCounter()
            }
            // Кнопка "Нет" (отрицательный ответ)
            .setNegativeButton(messageNegativeB, null)
            .show()
    }

    // метод сброса счетчика
    private fun resetTapCounter() {
        // Получаем доступ к SharedPreferences, используя константы из MainActivity
        val prefs = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putInt(MainActivity.TAP_COUNT_KEY, 0) }
        val toastMessReset = getString(R.string._toastMessReset)

        // Показываем пользователю сообщение, что все готово
        Toast.makeText(this, toastMessReset, Toast.LENGTH_SHORT).show()
    }

    // Обработка нажатия на кнопку "Назад" в ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}