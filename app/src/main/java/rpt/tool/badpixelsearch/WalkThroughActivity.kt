package rpt.tool.badpixelsearch

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rpt.tool.badpixelsearch.databinding.ActivityWalkThroughBinding
import rpt.tool.badpixelsearch.utils.AppUtils

class WalkThroughActivity : AppCompatActivity() {


    private var viewPagerAdapter: WalkThroughAdapter? = null
    private lateinit var binding: ActivityWalkThroughBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences(
            AppUtils.USERS_SHARED_PREF,
            AppUtils.PRIVATE_MODE)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        viewPagerAdapter = WalkThroughAdapter(supportFragmentManager)
        binding.walkThroughPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.walkThroughPager)
    }

    override fun onStart() {
        super.onStart()
        binding.getStarted.setOnClickListener {

            val edit = sharedPref.edit()
            edit.putBoolean(AppUtils.FIRST_RUN_KEY,false)
            edit.apply()
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