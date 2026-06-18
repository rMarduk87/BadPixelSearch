package rpt.tool.badpixelsearch.ui.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentOtherAppsBinding
import rpt.tool.badpixelsearch.utils.data.OtherApps
import rpt.tool.badpixelsearch.utils.view.adapters.OtherAppsAdapter
import androidx.core.net.toUri
import rpt.com.base.log.e
import rpt.com.base.log.i
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate
import rpt.tool.badpixelsearch.R

class OtherAppsFragment: BaseFragment<FragmentOtherAppsBinding>(FragmentOtherAppsBinding::inflate,false) {

    var other: ArrayList<OtherApps> = ArrayList()
    var adapter: OtherAppsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(rpt.tool.badpixelsearch.R.string.other_apps))

        val appPongClock = OtherApps()
        appPongClock.name = getString(rpt.tool.badpixelsearch.R.string.app_pong_clock_name)
        appPongClock.id = "0"
        appPongClock.link = getString(rpt.tool.badpixelsearch.R.string.app_pong_clock_link)

        val appMarimoCare = OtherApps()
        appMarimoCare.name = getString(rpt.tool.badpixelsearch.R.string.app_marimo_care_name)
        appMarimoCare.id = "1"
        appMarimoCare.link = getString(rpt.tool.badpixelsearch.R.string.app_marimo_care_link)

        val appLogViewerPro = OtherApps()
        appLogViewerPro.name = getString(rpt.tool.badpixelsearch.R.string.app_log_viewer_pro)
        appLogViewerPro.id = "2"
        appLogViewerPro.link = getString(rpt.tool.badpixelsearch
            .R.string.app_log_viewer_pro_link)

        other.add(appPongClock)
        other.add(appMarimoCare)
        other.add(appLogViewerPro)

        binding.otherAppsRecyclerView.isNestedScrollingEnabled = false

        adapter = OtherAppsAdapter(requireActivity(), other, object : OtherAppsAdapter.CallBack {

            override fun onClickSelect(other: OtherApps, position: Int) {
                try {
                    startActivity(Intent(
                        Intent.ACTION_VIEW,
                        other.link!!.toUri()))
                } catch (e: Exception) {
                    e.message?.let { e(Throwable(e), it) }
                }
            }

        })

        binding.otherAppsRecyclerView.setLayoutManager(
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.otherAppsRecyclerView.setAdapter(adapter)


        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val TAG = "nested_sync"
                if (scrollY > oldScrollY) {
                    i(TAG, "Scroll DOWN")
                }
                if (scrollY < oldScrollY) {
                    i(TAG, "Scroll UP")
                }

                if (scrollY == 0) {
                    i(TAG, "TOP SCROLL")
                }
                if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                    i(TAG, "BOTTOM SCROLL")

                }
            })
    }

    private fun finish() {
        safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(OtherAppsFragmentDirections
            .actionOtherAppsFragmentToMenuFragment())
    }
}