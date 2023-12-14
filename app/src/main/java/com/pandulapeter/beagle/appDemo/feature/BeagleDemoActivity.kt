package com.pandulapeter.beagle.appDemo.feature

import android.animation.Animator
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
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
        super.onCreate(savedInstanceState)
        handleSplashScreen()
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

    private fun handleSplashScreen() = installSplashScreen().setOnExitAnimationListener { splashScreen ->
        splashScreen.view.animate()
            .scaleX(2f)
            .scaleY(2f)
            .alpha(0f)
            .setInterpolator(AccelerateInterpolator())
            .setListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) = Unit
                    override fun onAnimationEnd(animator: Animator) = splashScreen.remove()
                    override fun onAnimationCancel(animator: Animator) = Unit
                    override fun onAnimationRepeat(animator: Animator) = Unit
                }
            )
            .start()
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
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