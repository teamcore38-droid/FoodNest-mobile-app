package com.umai.foodnest.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentCheckoutBinding
import com.umai.foodnest.viewmodel.CartViewModel

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val DELIVERY_FEE = 200.0

        cartViewModel.cartTotal.observe(viewLifecycleOwner) { subtotal ->
            binding.tvOrderSubtotal.text = "LKR %.2f".format(subtotal)
            binding.tvDeliveryFee.text = "LKR %.2f".format(DELIVERY_FEE)
            binding.tvOrderTotal.text = "LKR %.2f".format(subtotal + DELIVERY_FEE)
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            val summary = items.joinToString("\n") {
                "${it.foodItem.name} x${it.quantity}  LKR %.2f".format(it.totalPrice)
            }
            binding.tvOrderSummary.text = summary
        }

        binding.btnPlaceOrder.setOnClickListener {
            val address = binding.etAddress.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(context, "Enter delivery address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val paymentSelected = binding.rgPayment.checkedRadioButtonId != -1
            if (!paymentSelected) {
                Toast.makeText(context, "Select payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "Order Placed! 🎉", Toast.LENGTH_SHORT).show()
            cartViewModel.clearCart()

            val bundle = Bundle().apply { putString("orderId", "1024") }
            findNavController().navigate(R.id.action_checkout_to_tracking, bundle)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}