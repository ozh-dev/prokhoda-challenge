package ru.ozh.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import ru.ozh.map.PlaceConstants.BOUNDS_BOTTOM_RIGHT
import ru.ozh.map.PlaceConstants.BOUNDS_TOP_LEFT
import ru.ozh.map.PlaceConstants.MAP_PLACES
import ru.ozh.map.controller.ControllerShop
import ru.ozh.map.controller.ControllerSpace
import ru.ozh.map.ktx.onSlide
import ru.ozh.map.ktx.onStateChanged
import ru.ozh.map.view.button.State
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var btmListBehavior: BottomSheetBehavior<RecyclerView>

    private val adapter = EasyAdapter()
    private val controllerShop = ControllerShop()
    private val controllerSpace = ControllerSpace()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        with(list_rv) {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        btmListBehavior = BottomSheetBehavior.from(list_rv)
            .apply {
                isHideable = true
                onSlide { _, slideOffset ->
                    map_overlay_layout.slideOffset = slideOffset
                }

                onStateChanged { _, newState ->
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> map_btn.show(State.Map)
                        BottomSheetBehavior.STATE_COLLAPSED -> map_btn.hide()
                        BottomSheetBehavior.STATE_HIDDEN -> map_btn.show(State.Close)
                    }
                }
            }

        map_btn.setOnClickListener {
            btmListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        btmListBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        map_btn.show(State.Map)

        map_overlay_layout.setOnTouchListener { _, _ ->
            btmListBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return@setOnTouchListener false
        }

        ItemList.create()
            .add(controllerSpace)
            .addAll(MAP_PLACES, controllerShop)
            .also(adapter::setItems)
    }

    override fun onMapReady(gMap: GoogleMap) {
        //setup style, bounds, size area, zoom for map
        gMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style
            )
        )

        val bounds = LatLngBounds.Builder()
            .include(BOUNDS_TOP_LEFT)
            .include(BOUNDS_BOTTOM_RIGHT)
            .build()

        map_overlay_layout.setupMap(gMap)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 50))
        gMap.moveCamera(CameraUpdateFactory.zoomTo(15.5f))

        //inflate mock places
        gMap.setOnCameraIdleListener {
            map_overlay_layout.setPlaces(MAP_PLACES, gMap)
            gMap.setOnCameraIdleListener(null)
        }

        //refresh whole markers on map move action
        gMap.setOnCameraMoveListener {
            map_overlay_layout.refresh()
        }
    }
}