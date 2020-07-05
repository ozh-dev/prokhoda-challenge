package ru.ozh.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        ItemList.create()
            .apply {
                repeat(10) {
                    add(it, controllerShop)
                }
            }
            .also(adapter::setItems)

        btmListBehavior = BottomSheetBehavior.from(list_rv)
        btmListBehavior.isHideable = true
        btmListBehavior.isFitToContents = false

        btmListBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                val wholeOffset = calculateOffsetRatio(slideOffset)
                Log.d("TAG", "Offset: $slideOffset")
                Log.d("TAG", "Offset: $wholeOffset")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

        })

    }

    //((input - min) * 100) / (max - min)
    fun calculateOffsetRatio(slideOffset: Float): Float {
        val offsetMin = -1
        val offsetMax = 1
        val result = ((slideOffset - offsetMin) * 100) / (offsetMax - offsetMin)
        return result / 100f
    }
}