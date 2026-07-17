package com.umai.foodnest.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentCartBinding
import com.umai.foodnest.viewmodel.CartViewModel

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()
    private val DELIVERY_FEE = 200.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = CartAdapter(
            onPlus = { item ->
                cartViewModel.addToCart(item.foodItem)   // ✅ was empty before
            },
            onMinus = { item ->
                cartViewModel.decreaseQuantity(item.foodItem)  // ✅ handles qty>1 AND removal
            }
        )

        binding.rvCartItems.layoutManager = LinearLayoutManager(context)
        binding.rvCartItems.adapter = adapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            // Submit a NEW list copy so DiffUtil always compares distinct objects
            adapter.submitList(items.toList())
            val isEmpty = items.isEmpty()
            binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvCartItems.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.btnCheckout.isEnabled = !isEmpty
        }

        cartViewModel.cartTotal.observe(viewLifecycleOwner) { subtotal ->
            binding.tvSubtotal.text = "LKR %.2f".format(subtotal)
            binding.tvTotal.text = "LKR %.2f".format(subtotal + DELIVERY_FEE)
        }

        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_checkout)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}