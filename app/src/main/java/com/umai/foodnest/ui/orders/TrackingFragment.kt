package com.umai.foodnest.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.umai.foodnest.R
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.data.model.OrderStatus
import com.umai.foodnest.databinding.FragmentTrackingBinding
import com.umai.foodnest.viewmodel.OrderViewModel

class TrackingFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel: OrderViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId") ?: "1024"
        val order = SampleData.sampleOrders.find { it.id == orderId }
            ?: SampleData.sampleOrders.first()

        orderViewModel.setCurrentOrder(order)
        binding.tvOrderTitle.text = "Order #${order.id} — ${order.restaurantName}"
        binding.tvETA.text = if (order.status == OrderStatus.DELIVERED)
            "✅ Delivered" else "🕐 Estimated: ${order.estimatedArrival} min"

        updateTrackingUI(order.status)

        binding.btnCancel.visibility =
            if (order.status == OrderStatus.CONFIRMED) View.VISIBLE else View.GONE
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        // Setup map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Colombo coordinates
        val colombo = LatLng(6.9271, 79.8612)
        val restaurant = LatLng(6.9300, 79.8650)
        val customer = LatLng(6.9200, 79.8550)

        map.addMarker(MarkerOptions().position(restaurant).title("Restaurant"))
        map.addMarker(MarkerOptions().position(customer).title("Your Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(colombo, 13f))
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun updateTrackingUI(status: OrderStatus) {
        val activeColor = resources.getColor(R.color.orange_primary, null)
        val inactiveColor = resources.getColor(R.color.gray_light, null)
        val stepIndex = when (status) {
            OrderStatus.CONFIRMED -> 0
            OrderStatus.PREPARING -> 1
            OrderStatus.ON_THE_WAY -> 2
            OrderStatus.DELIVERED -> 3
            else -> 0
        }
        listOf(
            binding.trackingSteps.lineStep2,
            binding.trackingSteps.lineStep3
        ).forEachIndexed { i, line ->
            line.setBackgroundColor(if (i < stepIndex) activeColor else inactiveColor)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}