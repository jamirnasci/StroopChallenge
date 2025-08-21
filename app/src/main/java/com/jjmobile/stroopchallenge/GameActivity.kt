package com.jjmobile.stroopchallenge

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjmobile.stroopchallenge.databinding.ActivityGameBinding
import kotlin.random.Random
import androidx.core.content.edit

class GameActivity : AppCompatActivity() {

    private lateinit var targetColor: TextView
    private lateinit var colorText: String
    private lateinit var interval: CountDownTimer
    private lateinit var binding: ActivityGameBinding
    private var pontuacao: Int = 0

    fun checkColor(name: String): Boolean{
        return name == colorText
    }

    fun randomName(){
        val index: Int = Random.nextInt(Colors.values.size)
        targetColor.setTextColor(ContextCompat.getColor(applicationContext, Colors.values[index].colorId))
        colorText = Colors.values[index].name
    }
    fun randomColor(){
        val index: Int = Random.nextInt(Colors.values.size)
        targetColor.text = Colors.values[index].name
        randomName()
    }

    fun startInterval(){
        interval = object : CountDownTimer(10000, 100){
            override fun onFinish() {
                binding.progressBar.progress = 0
                endGameAlert()
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.progress = binding.progressBar.progress - 1
            }

        }
        interval.start()
    }

    fun goTo(){

    }

    fun endGameAlert(){
        interval.cancel()

        MaterialAlertDialogBuilder(this)
            .setTitle("Fim de Jogo")
            .setMessage("Sua pontuação foi $pontuacao")
            .setPositiveButton("OK") { dialog, _ ->
                val endGameIntent = Intent(applicationContext, EndGameActivity::class.java)
                endGameIntent.putExtra("pontuacao", pontuacao)
                startActivity(endGameIntent)
            }
            .show()
    }

    fun nextRound(){
        pontuacao += 10
        binding.pontuacaoText.text = pontuacao.toString()
        randomColor()
        interval.cancel()
        startInterval()
        binding.progressBar.progress = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val record: Int = sharedPreferences.getInt("record", 0)
        binding.recordText.text = record.toString()

        targetColor = binding.targetColor
        randomColor()
        startInterval()

        binding.blueBtn.setOnClickListener {
            val result = checkColor("BLUE")
            if(result){
                nextRound()
            }else{
                endGameAlert()
            }
        }
        binding.redBtn.setOnClickListener {
            val result = checkColor("RED")
            if(result){
                nextRound()
            }else{
                endGameAlert()
            }
        }
        binding.greenBtn.setOnClickListener {
            val result = checkColor("GREEN")
            if(result){
                nextRound()
            }else{
                endGameAlert()
            }
        }
        binding.yellowBtn.setOnClickListener {
            val result = checkColor("YELLOW")
            if(result){
                nextRound()
            }else{
                endGameAlert()
            }
        }
    }
}