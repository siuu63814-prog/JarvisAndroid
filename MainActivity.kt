package com.alamin.jarvis

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var status: TextView
    private lateinit var tts: TextToSpeech
    private val requestSpeech = 101
    private val requestPermission = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 36, 28, 36)
        }
        val title = TextView(this).apply {
            text = "J.A.R.V.I.S"
            textSize = 32f
            gravity = Gravity.CENTER
        }
        status = TextView(this).apply {
            text = "Ready. Tap the button and speak."
            textSize = 17f
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 28)
        }
        val talk = Button(this).apply {
            text = "TAP TO TALK"
            textSize = 18f
            setOnClickListener { startListening() }
        }
        layout.addView(title)
        layout.addView(status)
        layout.addView(talk)
        setContentView(layout)
    }

    private fun startListening() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestPermission)
            return
        }
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "bn-BD")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "বলুন, কী করতে হবে?")
            }
            startActivityForResult(intent, requestSpeech)
        } catch (_: Exception) {
            status.text = "Speech recognition is unavailable on this phone."
            speak("Speech recognition is not available.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestPermission && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening()
        } else {
            status.text = "Microphone permission is needed to hear commands."
        }
    }

    @Deprecated("Deprecated by Android, retained for broad compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestSpeech && resultCode == RESULT_OK) {
            val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull().orEmpty()
            status.text = if (spoken.isBlank()) "I didn't catch that." else "You said: $spoken"
            if (spoken.isNotBlank()) handleCommand(spoken.lowercase(Locale.ROOT))
        }
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("youtube") || command.contains("ইউটিউব") -> openApp("com.google.android.youtube", "Opening YouTube")
            command.contains("facebook") || command.contains("ফেসবুক") -> openApp("com.facebook.katana", "Opening Facebook")
            command.contains("chrome") || command.contains("ক্রোম") -> openApp("com.android.chrome", "Opening Chrome")
            command.contains("play store") || command.contains("প্লে স্টোর") -> openApp("com.android.vending", "Opening Play Store")
            command.contains("setting") || command.contains("সেটিংস") -> {
                speak("Opening settings")
                startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
            }
            command.contains("time") || command.contains("সময়") || command.contains("কয়টা বাজে") -> {
                val now = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(java.util.Date())
                speak("The time is $now")
                status.text = "Time: $now"
            }
            command.contains("hello") || command.contains("হ্যালো") || command.contains("জার্ভিস") -> speak("Hello. I am ready.")
            else -> speak("I can open YouTube, Facebook, Chrome, Play Store, Settings, and tell the time.")
        }
    }

    private fun openApp(packageName: String, message: String) {
        val launch = packageManager.getLaunchIntentForPackage(packageName)
        if (launch != null) {
            speak(message)
            startActivity(launch)
        } else {
            status.text = "That app is not installed."
            speak("That app is not installed.")
        }
    }

    private fun speak(message: String) {
        if (::tts.isInitialized) tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "jarvis_reply")
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts.language = Locale("en", "US")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) tts.shutdown()
        super.onDestroy()
    }
}
