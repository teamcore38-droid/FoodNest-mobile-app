package com.umai.foodnest.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umai.foodnest.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId") ?: "1024"
        binding.tvOrderRef.text = "Order #$orderId"

        binding.btnSubmitReview.setOnClickListener {
            val foodRating = binding.ratingFood.rating
            val deliveryRating = binding.ratingDelivery.rating
            val comment = binding.etComment.text.toString()

            if (foodRating == 0f || deliveryRating == 0f) {
                Toast.makeText(context, "Please rate your experience", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "Thank you for your review! ⭐", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}