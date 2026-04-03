package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        val inflate = ActivityInputBinding.inflate(getLayoutInflater())
        this.binding = inflate
        setContentView(inflate.getRoot())


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
