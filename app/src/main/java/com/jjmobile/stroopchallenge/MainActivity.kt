package com.jjmobile.stroopchallenge

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jjmobile.stroopchallenge.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var level = 0

    private val backgrounds = listOf(
        R.drawable.red_color_button,
        R.drawable.blue_color_button,
        R.drawable.green_color_button,
        R.drawable.yellow_color_button,
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val record: Int = sharedPreferences.getInt("record", 0)

        if(record <= 0){
            binding.recordCard.visibility = View.GONE
        }else{
            binding.startRecord.text = record.toString()
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
        
        binding.startGame.setOnClickListener { 
            val gameIntent: Intent = Intent(applicationContext, GameActivity::class.java)
            gameIntent.putExtra("level", level)
            startActivity(gameIntent)
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