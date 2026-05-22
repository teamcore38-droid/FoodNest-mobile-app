package com.umai.foodnest.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentOrdersBinding
import com.umai.foodnest.viewmodel.OrderViewModel

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel.loadOrders()

        val adapter = OrderAdapter(
            onTrack = { order ->
                val bundle = Bundle().apply {
                    putString("orderId", order.id)
                }
                findNavController().navigate(
                    R.id.action_orders_to_tracking, bundle
                )
            },
            onReorder = { order ->
                findNavController().navigate(R.id.homeFragment)
            }
        )

        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.adapter = adapter

        orderViewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.submitList(orders)
            binding.emptyState.visibility =
                if (orders.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}