package com.jjmobile.stroopchallenge.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jjmobile.stroopchallenge.EndGameActivity

class EndGameInterstitial(
    val context: Context,
    val INTERSTITIAL_ID: String,
    val activity: EndGameActivity
) {
    var interstitialAd: InterstitialAd? = null

    fun loadInterstitial(){
        InterstitialAd.load(
            context,
            INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("INTERSTITIAL", "Ad was loaded.")
                    interstitialAd = ad
                    interstitialAd?.show(activity)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("INTERSTITIAL", adError.message)
                    interstitialAd = null
                }
            },
        )
    }
    fun setContentCallBack(){
        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("INTERSTITIAL", "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    Log.d("INTERSTITIAL", "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d("INTERSTITIAL", "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("INTERSTITIAL", "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    // Called when ad is clicked.
                    Log.d("INTERSTITIAL", "Ad was clicked.")
                }
            }
    }
}