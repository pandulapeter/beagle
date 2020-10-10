package com.pandulapeter.beagle.appDemo.feature

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ActivityBeagleDemoBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainFragment
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.visible
import com.pandulapeter.beagle.utils.extensions.dimension

class BeagleDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeagleDemoBinding
    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beagle_demo)
        setupEdgeToEdge()
        binding.beagleButton.setOnClickListener {
            Beagle.show()
            binding.debugMenu.visible = !binding.debugMenu.visible
        }
        if (savedInstanceState == null) {
            supportFragmentManager.handleReplace(
                transitionType = null,
                newInstance = MainFragment.Companion::newInstance
            )
        }
    }

    override fun onBackPressed() {
        if (binding.debugMenu is FrameLayout && binding.debugMenu.visible) {
            binding.debugMenu.visible = false
        } else if (!Beagle.hide() && currentFragment?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.beagleButton, null)
        super.onDestroy()
    }

    private fun setupEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setDecorFitsSystemWindows(false)
            binding.beagleButton.run {
                val margin = dimension(R.dimen.beagle_button_margin)
                ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
                    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                        bottomMargin = margin + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                    }
                    insets
                }
            }
        }
    }
}