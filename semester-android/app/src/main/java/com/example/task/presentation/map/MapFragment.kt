package com.example.task.presentation.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentMapBinding
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private var mapView : MapView? = null

    private var coordinates : Pair<Double, Double>? = null

    private var isPointEditable : Boolean? = null
    private var placeMark : PlacemarkMapObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.map
        isPointEditable = arguments?.getBoolean(IS_EDITABLE_KEY) ?: false
        val latitude = arguments?.getDouble(LATITUDE_KEY) ?: throw NoSuchElementException()
        val longitude = arguments?.getDouble(LONGITUDE_KEY) ?: throw NoSuchElementException()
        coordinates = Pair(latitude, longitude)
        initMapView(latitude, longitude)
        binding.submitBtn.setOnClickListener {
            setFragmentResult(MAP_POINT_SELECT_KEY, Bundle().apply {
                coordinates?.let {
                    putDouble(LATITUDE_KEY, it.first)
                    putDouble(LONGITUDE_KEY, it.second)
                }
            })
            findNavController().navigateUp()
        }
    }

    private fun initMapView(latitude : Double, longitude : Double) {
        mapView?.run {
            mapWindow.map.move(
                CameraPosition(
                    Point(latitude, longitude),
                    17.0f,
                    0.0f,
                    0.0f
                )
            )

            val image = ImageProvider.fromResource(requireContext(), R.drawable.mark_medium)
            placeMark = mapWindow.map.mapObjects.addCollection().addPlacemark().apply {
                geometry = Point(latitude, longitude)
                opacity = 1.0f
                setIcon(image)
            }

            mapWindow.map.addTapListener { tapEvent ->
                if (isPointEditable != null && isPointEditable == true) {
                    val point = tapEvent.geoObject.geometry.firstOrNull()?.point ?: return@addTapListener true
                    coordinates = Pair(point.latitude, point.longitude)
                    placeMark?.geometry = point
                    binding.submitBtn.isVisible = true
                }
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    companion object {
        const val LATITUDE_KEY = "LATITUDE_KEY"
        const val LONGITUDE_KEY = "LONGITUDE_KEY"
        const val MAP_POINT_SELECT_KEY = "MAP_POINT_SELECT_KEY"
        const val IS_EDITABLE_KEY = "IS_EDITABLE_KEY"
    }
}