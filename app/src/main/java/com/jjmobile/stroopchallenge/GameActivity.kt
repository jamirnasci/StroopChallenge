package com.jjmobile.stroopchallenge

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjmobile.stroopchallenge.databinding.ActivityGameBinding
import kotlin.random.Random

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
    private var colors: MutableList<Color> = Colors.list.toMutableList()
    private lateinit var soundPool: SoundPool

    private var matchSound: Int = 0
    private var failSound: Int = 0

    fun checkColor(name: String): Boolean{
        return name == colorText
    }
    fun randomName(){
        val index: Int = Random.nextInt(colors.size)
        targetColor.setTextColor(ContextCompat.getColor(applicationContext, colors[index].colorId))
        colorText = colors[index].name
    }
    fun randomColor(){
        val index: Int = Random.nextInt(colors.size)
        targetColor.text = colors[index].name
        binding.progressBar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colors[index].colorId))
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

        soundPool.play(failSound, 1f, 1f, 1, 0, 1f)
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

        binding.pinkBtn.visibility = View.GONE
        binding.purpleBtn.visibility = View.GONE

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME) // uso comum para efeitos
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5) // número máximo de sons simultâneos
            .setAudioAttributes(audioAttributes)
            .build()

        matchSound = soundPool.load(this, R.raw.match, 1)
        failSound = soundPool.load(this, R.raw.fail, 1)

        when(level){
            0 -> {
                countDownDec = 10
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue))
                binding.levelText.text = "Level Easy"
            }
            1 -> {
                countDownDec = 30
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.yellow100))
                binding.levelText.text = "Level Medium"
            }
            2 -> {
                countDownDec = 40
                binding.levelText.setTextColor(ContextCompat.getColor(applicationContext,R.color.red))
                binding.levelText.text = "Level Hard"
                colors.add(Color("PURPLE", R.color.purple))
                colors.add(Color("PINK", R.color.pink))
                binding.pinkBtn.visibility = View.VISIBLE
                binding.purpleBtn.visibility = View.VISIBLE
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
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.redBtn.setOnClickListener {
            val result = checkColor("RED")
            if(result){
                nextRound()
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.greenBtn.setOnClickListener {
            val result = checkColor("GREEN")
            if(result){
                nextRound()
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.yellowBtn.setOnClickListener {
            val result = checkColor("YELLOW")
            if(result){
                nextRound()
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.purpleBtn.setOnClickListener {
            val result = checkColor("PURPLE")
            if(result){
                nextRound()
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
            }else{
                killEvents()
                endGameAlert()
            }
        }
        binding.pinkBtn.setOnClickListener {
            val result = checkColor("PINK")
            if(result){
                nextRound()
                soundPool.play(matchSound, 1f, 1f, 1, 0, 1f)
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