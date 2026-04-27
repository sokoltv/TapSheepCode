package ru.sokoltv.tapsheep

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Privacy Policy"

        val privacyPolicyContentTextView: TextView = findViewById(R.id.privacyPolicyContentTextView)

        // Читаем текстовый файл из папки assets и выводим его в TextView
        try {
            val inputStream = assets.open("privacy_policy.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            privacyPolicyContentTextView.text = stringBuilder.toString()
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            privacyPolicyContentTextView.text = "Error loading Privacy Policy."
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}