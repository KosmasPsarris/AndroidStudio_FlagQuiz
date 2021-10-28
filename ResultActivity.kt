package com.p17191.ergasies.flagquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.p17191.ergasies.flagquiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        val binding : ActivityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUsername.text = intent.getStringExtra(Constants.USER_NAME)

        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        binding.tvScore.text = getString(R.string.score_is, correctAnswers.toString(), totalQuestions.toString())

        binding.finishButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}