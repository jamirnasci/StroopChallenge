package com.jjmobile.stroopchallenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jjmobile.stroopchallenge.ads.EndGameInterstitial
import com.jjmobile.stroopchallenge.databinding.ActivityEndGameBinding

class EndGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding

    private val INTERSTITIAL_ID_TEST = "ca-app-pub-3940256099942544/1033173712"
    private val INTERSTITIAL_ID = "ca-app-pub-2077187211919243/8828710577"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val endGameInterstitial = EndGameInterstitial(applicationContext, INTERSTITIAL_ID, this@EndGameActivity)
        endGameInterstitial.loadInterstitial()
        endGameInterstitial.setContentCallBack()

        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val record: Int = sharedPreferences.getInt("record", 0)

        val pontuacao = intent.getIntExtra("pontuacao", 0)
        val level = intent.getIntExtra("level", 0)
        sharedPreferences.edit {
            if(record == 0 || pontuacao > record){
                putInt("record", pontuacao)
                putInt("level", level)
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