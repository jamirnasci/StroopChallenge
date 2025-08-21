package com.jjmobile.stroopchallenge

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jjmobile.stroopchallenge.databinding.ActivityEndGameBinding

class EndGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val record: Int = sharedPreferences.getInt("record", 0)

        val pontuacao = intent.getIntExtra("pontuacao", 0)

        sharedPreferences.edit {
            if(record == 0 || pontuacao > record){
                putInt("record", pontuacao)
            }else{
                binding.novoRecordText.visibility = View.GONE
            }
        }
        binding.endGamePontuacao.text = pontuacao.toString()
        binding.menuBtn.setOnClickListener {
            val menuIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(menuIntent)
        }
    }
}