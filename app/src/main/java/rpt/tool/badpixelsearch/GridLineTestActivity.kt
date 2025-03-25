package rpt.tool.badpixelsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rpt.tool.badpixelsearch.databinding.ActivityGridLineTestBinding


class GridLineTestActivity : AppCompatActivity() {

    lateinit var binding: ActivityGridLineTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGridLineTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}