package com.jjmobile.stroopchallenge

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
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
    private var countDownInterval = 1000L
    private var countDownDec = 10
    private var progressDec = 10
    private var limitTime = 10000L
    private var level = 0
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
        binding.progressBar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, Colors.values[index].colorId))
        randomName()
    }

    fun startInterval(){
        interval = object : CountDownTimer(limitTime, countDownInterval){
            override fun onFinish() {
                binding.progressBar.progress = 0
                endGameAlert()
            }

            override fun onTick(millisUntilFinished: Long) {
                if(binding.progressBar.progress <= 0){
                    killEvents()
                    endGameAlert()
                }
                binding.progressBar.setProgress(binding.progressBar.progress - progressDec, true)
            }

        }
        interval.start()
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    fun endGameAlert(){
        interval.cancel()

        val view = layoutInflater.inflate(R.layout.end_game_dialog, null)
        val okBtn = view.findViewById<Button>(R.id.okBtn)
        val scoreMsg = view.findViewById<TextView>(R.id.scoreMsg)
        scoreMsg.text = "Your Score $pontuacao"
        okBtn.setOnClickListener {
            val endGameIntent = Intent(applicationContext, EndGameActivity::class.java)
            endGameIntent.putExtra("pontuacao", pontuacao)
            endGameIntent.putExtra("level", level)
            startActivity(endGameIntent)
        }
        MaterialAlertDialogBuilder(this)
            .setView(view)
            .show()
    }

    fun nextRound(){
        pontuacao += 10
        countDownInterval -= countDownDec
        binding.pontuacaoText.text = pontuacao.toString()
        randomColor()
        interval.cancel()
        startInterval()
        binding.progressBar.progress = 100
    }
    fun killEvents(){
        binding.blueBtn.setOnClickListener(null)
        binding.redBtn.setOnClickListener(null)
        binding.greenBtn.setOnClickListener(null)
        binding.yellowBtn.setOnClickListener(null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        level = intent.getIntExtra("level", 0)

        when(level){
            0 -> {
                countDownDec = 10
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue))
                binding.levelText.text = "Level Easy"
            }
            1 -> {
                countDownDec = 20
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.yellow100))
                binding.levelText.text = "Level Medium"
            }
            2 -> {
                countDownDec = 30
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.red))
                binding.levelText.text = "Level Hard"
            }
        }
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
                killEvents()
                endGameAlert()
            }
        }
        binding.redBtn.setOnClickListener {
            val result = checkColor("RED")
            if(result){
                nextRound()
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.greenBtn.setOnClickListener {
            val result = checkColor("GREEN")
            if(result){
                nextRound()
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.yellowBtn.setOnClickListener {
            val result = checkColor("YELLOW")
            if(result){
                nextRound()
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.exitGameBtn.setOnClickListener {
            interval.cancel()
            finish()
        }
    }
}