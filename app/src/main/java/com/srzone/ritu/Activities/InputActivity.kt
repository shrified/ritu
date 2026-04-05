package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.srzone.ritu.Adapters.OnBoardingFragmentsAdapter
import com.srzone.ritu.R
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {
    var binding: ActivityInputBinding? = null
    @JvmField
    var cyclesLength: Int = 0
    @JvmField
    var periodLength: Int = 0


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)


        enableEdgeToEdge()
        // set the status bar icon colors to white
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        val inflate = ActivityInputBinding.inflate(getLayoutInflater())
        this.binding = inflate
        setContentView(inflate.getRoot())



        // This is the cleanest, most "Senior" way
        val rootView = findViewById<View>(R.id.rootLayout)
        val statusBarBackground = findViewById<View>(R.id.statusBarBackground)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { rootView, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // 1. PROTECT THE SIDES: Apply left/right padding to the root layout.
            // This prevents content from hiding under the notch in landscape.
            // We leave top as 0 because we handle it manually below.
            // No Bottom Navigation, so directly pad on the rootView layout
            rootView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            // 2. Top STATUS BAR: Match the fake status bar height to the top inset
            statusBarBackground.updateLayoutParams {
                height = systemBars.top
            }

            insets
        }

        //        AdsGoogle adsGoogle = new AdsGoogle( this);
//        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
//        adsGoogle.Interstitial_Show_Counter(this);
        getWindow().setFlags(512, 256)
        if (!SharedPreferenceUtils.getDate(this).isEmpty() && !SharedPreferenceUtils.getCycles(this)
                .isEmpty() && !getIntent().getBooleanExtra("recalculate", false)
        ) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
            return
        }
        this.binding!!.viewPager.setOffscreenPageLimit(3)
        this.binding!!.viewPager.setUserInputEnabled(false)
        Utils.setStatusBarColor(R.color.input_screen_bg_color, this)
        binding!!.viewPager.adapter = OnBoardingFragmentsAdapter(
            supportFragmentManager,
            lifecycle
        )
    }
}
