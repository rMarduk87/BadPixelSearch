package rpt.tool.badpixelsearch.ui.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentOtherAppsBinding
import rpt.tool.badpixelsearch.utils.data.OtherApps
import rpt.tool.badpixelsearch.utils.log.i
import rpt.tool.badpixelsearch.utils.log.e
import rpt.tool.badpixelsearch.utils.view.adapters.OtherAppsAdapter
import androidx.core.net.toUri
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class OtherAppsFragment: BaseFragment<FragmentOtherAppsBinding>(FragmentOtherAppsBinding::inflate) {

    var other: ArrayList<OtherApps> = ArrayList()
    var adapter: OtherAppsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftIconBlock.setOnClickListener{ finish() }

        val appWaterDiary = OtherApps()
        appWaterDiary.name = "Water Diary"
        appWaterDiary.id = "0"
        appWaterDiary.link = "https://play.google.com/store/apps/details?id=rpt.tool.mementobibere"

        val appPongClock = OtherApps()
        appPongClock.name = "Pong Clock"
        appPongClock.id = "1"
        appPongClock.link = "https://play.google.com/store/apps/details?id=rpt.tool.pongclock"

        other.add(appWaterDiary)
        other.add(appPongClock)

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
        safeNavController?.safeNavigate(OtherAppsFragmentDirections
            .actionOtherAppsFragmentToMenuFragment())
    }
}