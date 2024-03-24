package rpt.tool.badpixelsearch

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rpt.tool.badpixelsearch.databinding.ActivityWalkThroughBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class WalkThroughActivity : AppCompatActivity() {


    private var viewPagerAdapter: WalkThroughAdapter? = null
    private lateinit var binding: ActivityWalkThroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        viewPagerAdapter = WalkThroughAdapter(supportFragmentManager)
        binding.walkThroughPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.walkThroughPager)
    }

    override fun onStart() {
        super.onStart()
        binding.getStarted.setOnClickListener {

            SharedPreferencesManager.firstRun = false
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    private inner class WalkThroughAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 4
        }

        override fun getItem(i: Int): Fragment {

            when (i) {
                0 -> {
                    return WalkThroughOne()
                }

                1 -> {
                    return WalkThroughTwo()
                }

                2 -> {
                    return WalkThroughThree()
                }
                3 -> {
                    return WalkThroughFour()
                }

                else -> {
                    return WalkThroughOne()
                }
            }

        }
    }


    class WalkThroughOne : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.walk_through_one, container, false)

        }
    }

    class WalkThroughTwo : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.walk_through_two, container, false)

        }
    }

    class WalkThroughThree : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.walk_through_three, container, false)

        }

    }

    class WalkThroughFour : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.walk_through_four, container, false)

        }

    }
}