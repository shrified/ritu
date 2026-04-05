package com.srzone.ritu.Activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.srzone.ritu.Adapters.FragmentsAdapter
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyCustomTheme
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    private val tabIcons = intArrayOf(
        R.drawable.ic_home,
        R.drawable.ic_calendar,
        R.drawable.ic_blogs,
        R.drawable.ic_settings,
        R.drawable.ic_settings_gear
    )
    var theme: MyCustomTheme? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        enableEdgeToEdge()
        // set the status bar icon colors to white
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        val inflate = ActivityMainBinding.inflate(getLayoutInflater())
        this.binding = inflate
        setContentView(inflate.getRoot())


        // This is the cleanest, most "Senior" way
        val rootView = findViewById<View>(R.id.mainParentLayout)
        val statusBarBackground = findViewById<View>(R.id.statusBarBackground)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            
            // Apply side padding for notches/landscape
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            
            // Fake status bar height
            statusBarBackground.updateLayoutParams {
                height = systemBars.top
            }

            // Remove bottom padding from rootView to avoid extra space under navigation card
            // The navigation card has its own margin and floats above the bottom.
            
            insets
        }

        val supportFragmentManager = getSupportFragmentManager()
        this.theme = MyThemeHandler().getAppTheme(this)
        binding!!.viewPager.adapter = FragmentsAdapter(
            supportFragmentManager,
            lifecycle
        )
        this.binding!!.viewPager.setOffscreenPageLimit(4)
        this.binding!!.viewPager.setUserInputEnabled(false)
        setUpTheme()
        val tabLayout = this.binding!!.tabLayout
        for (i in this.tabIcons) {
            tabLayout.addTab(tabLayout.newTab().setIcon(i))
        }
        for (i2 in 0..<tabLayout.getTabCount()) {
            tabLayout.getTabAt(i2)!!.getIcon()!!.setColorFilter(
                ContextCompat.getColor(this, this.theme!!.themeColor),
                PorterDuff.Mode.SRC_IN
            )
        }
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                this@MainActivity.binding!!.viewPager.setCurrentItem(tab.getPosition())
            }
        })
        this.binding!!.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(i3: Int) {
                val tabLayout2 = tabLayout
                tabLayout2.selectTab(tabLayout2.getTabAt(i3))
            }
        })
    }

    private fun setUpTheme() {
        Utils.makeTransparentStatusBar(this)
        this.binding!!.mainParentLayout.setBackground(getResources().getDrawable(this.theme!!.bgImg))
        this.binding!!.tabLayout.setSelectedTabIndicatorColor(getResources().getColor(this.theme!!.themeColor))
        
        // Ensure navigationCard is transparent to avoid any background bleeding
        binding!!.navigationCard.setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    override fun onBackPressed() {
        val currentItem = this.binding!!.viewPager.getCurrentItem()

        if (currentItem != 0) {
            this.binding!!.viewPager.setCurrentItem(0)
        } else {
            super.onBackPressed()
        }
    }
}
