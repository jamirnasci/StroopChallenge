package com.jjmobile.stroopchallenge

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        val level = intent.getIntExtra("level", 0)
        sharedPreferences.edit {
            if(record == 0 || pontuacao > record){
                putInt("record", pontuacao)
            }else{
                binding.novoRecordText.visibility = View.GONE
                binding.novoRecordImg.visibility = View.GONE
            }
        }
        binding.endGamePontuacao.text = pontuacao.toString()
        binding.menuBtn.setOnClickListener {
            val menuIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(menuIntent)
        }
        binding.playAgainBtn.setOnClickListener {
            val playAgainIntent = Intent(applicationContext, GameActivity::class.java)
            playAgainIntent.putExtra("level", level)
            startActivity(playAgainIntent)
        }
        when(level){
            0 -> {
                binding.levelEndGameText.text = "Level Easy"
                binding.levelEndGameText.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue))
            }
            1 -> {
                binding.levelEndGameText.text = "Level Medium"
                binding.levelEndGameText.setTextColor(ContextCompat.getColor(applicationContext,R.color.yellow200))
            }
            2 -> {
                binding.levelEndGameText.text = "Level Hard"
                binding.levelEndGameText.setTextColor(ContextCompat.getColor(applicationContext,R.color.red))
            }
        }
    }
}