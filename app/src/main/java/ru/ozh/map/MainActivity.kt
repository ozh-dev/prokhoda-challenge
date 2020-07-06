package ru.ozh.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import ru.ozh.map.controller.ControllerShop
import ru.ozh.map.controller.ControllerSpace
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val adapter = EasyAdapter()
    private val controllerSpace = ControllerSpace()
    private val controllerShop = ControllerShop()

    private lateinit var btmListBehavior: BottomSheetBehavior<RecyclerView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(list_rv) {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        btmListBehavior = BottomSheetBehavior.from(list_rv)
        btmListBehavior.isHideable = true

        btmListBehavior.onSlide { _, slideOffset ->
//            map_overlay_layout.ratio = 1 - calculateOffsetRatio(slideOffset)
            map_overlay_layout.slideOffset = slideOffset
        }

        ItemList.create()
            .apply {
                repeat(10) {
                    add(it, controllerShop)
                }
            }
            .also(adapter::setItems)

        open_btn.setOnClickListener {
            btmListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    //((input - min) * 100) / (max - min)
    private fun calculateOffsetRatio(@FloatRange(from = -1.0, to = 1.0) slideOffset: Float): Float {
        val offsetMin = -1
        val offsetMax = 1
        val result = ((slideOffset - offsetMin) * 100) / (offsetMax - offsetMin)
        return result / 100f
    }
}