package com.jjmobile.stroopchallenge

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.jjmobile.stroopchallenge.databinding.ActivityMainBinding
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adView: AdView
    private val BANNER_ID = "ca-app-pub-2077187211919243/3071866649"
    private val BANNER_ID_TEST = "ca-app-pub-3940256099942544/9214589741"
    private var level = 0
    private val backgrounds = listOf(
        R.drawable.red_color_button,
        R.drawable.blue_color_button,
        R.drawable.green_color_button,
        R.drawable.yellow_color_button,
        R.drawable.pink_button_btn,
        R.drawable.purple_color_button
    )

    fun animateDrawableChange(view: View, newDrawable: Int, duration: Int = 600) {
        val currentDrawable = view.background ?: ContextCompat.getDrawable(view.context, newDrawable)

        val layers = arrayOf(
            currentDrawable,
            ContextCompat.getDrawable(view.context, newDrawable)
        )

        val transitionDrawable = TransitionDrawable(layers)
        view.background = transitionDrawable
        transitionDrawable.startTransition(duration)
    }
    fun randomBackground(): Int{
        val index = Random.nextInt(backgrounds.size)
        return backgrounds[index]
    }
    fun startGameActivity(){
        val gameIntent = Intent(applicationContext, GameActivity::class.java)
        gameIntent.putExtra("level", level)
        adView.destroy()
        startActivity(gameIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(applicationContext)

        val adView = AdView(this)
        adView.adUnitId = BANNER_ID
        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360))
        this.adView = adView
        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)

        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        OneSignal.initWithContext(this, "493483a6-2423-4254-b3a1-50fe91a4313a")
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener(){
            override fun onAdImpression() {
                super.onAdOpened()
                binding.startGame.alpha = 1f
                binding.startGame.setOnClickListener {
                    startGameActivity()
                }
            }
        }

        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val record: Int = sharedPreferences.getInt("record", -1)
        val recordLevel: Int = sharedPreferences.getInt("level", -1)

        if(record == -1 || recordLevel == -1){
            binding.recordCard.visibility = View.GONE
        }else{
            binding.startRecord.text = record.toString()
            when(recordLevel){
                0 -> {
                    binding.levelRecordText.text = "Easy"
                    binding.levelRecordText.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue))
                }
                1 -> {
                    binding.levelRecordText.text = "Medium"
                    binding.levelRecordText.setTextColor(ContextCompat.getColor(applicationContext,R.color.yellow200))
                }
                2 -> {
                    binding.levelRecordText.text = "Hard"
                    binding.levelRecordText.setTextColor(ContextCompat.getColor(applicationContext,R.color.red))
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
                while(true){
                    animateDrawableChange(binding.square1, randomBackground())
                    animateDrawableChange(binding.square2, randomBackground())
                    animateDrawableChange(binding.square3, randomBackground())
                    animateDrawableChange(binding.square4, randomBackground())
                    delay(1000L)
                }
        }

        binding.levelSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if(progress == 1){
                    seekBar?.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.yellow))
                }else if(progress == 2){
                    seekBar?.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.red))
                }
                level = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })
    }
}