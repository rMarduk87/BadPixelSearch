package rpt.tool.badpixelsearch

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import rpt.tool.badpixelsearch.utils.Inflate
import rpt.tool.badpixelsearch.utils.log.e
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setupToolbar(btnBack: View, menuTitle: TextView? = null, title: String? = null) {
        title?.let { menuTitle?.text = it }
        btnBack.setOnClickListener {
            try {
                if (SharedPreferencesManager.sound) {
                    val mediaPlayer = MediaPlayer.create(requireContext(),
                        R.raw.goodbye)
                    mediaPlayer?.setOnCompletionListener { it.release() }
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                e(Throwable(e), "Sound")
            }
            safeNavController?.popBackStack()
        }
    }

}
