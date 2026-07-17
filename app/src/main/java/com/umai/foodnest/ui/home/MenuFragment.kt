package com.umai.foodnest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.umai.foodnest.databinding.FragmentMenuBinding
import com.umai.foodnest.viewmodel.CartViewModel
import com.umai.foodnest.viewmodel.MenuViewModel

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val menuViewModel: MenuViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()
        menuViewModel.loadMenu(arguments?.getInt("restaurantId") ?: 1)
        observeData()
        setupFab()
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter { foodItem ->
            cartViewModel.addToCart(foodItem)
            Toast.makeText(context, "${foodItem.name} added to cart", Toast.LENGTH_SHORT).show()
            binding.fabCart.visibility = View.VISIBLE
        }
        binding.rvMenuItems.layoutManager = LinearLayoutManager(context)
        binding.rvMenuItems.adapter = foodAdapter
    }

    private fun observeData() {
        menuViewModel.restaurant.observe(viewLifecycleOwner) { restaurant ->
            binding.tvRestaurantName.text = restaurant.name
            binding.tvRestaurantRating.text = "⭐ ${restaurant.rating}"
            binding.tvRestaurantTime.text = "🕐 ${restaurant.deliveryTime} min"
            Glide.with(this).load(restaurant.imageUrl).into(binding.ivRestaurantBanner)
            setupCategoryChips()
        }

        menuViewModel.filteredItems.observe(viewLifecycleOwner) { items ->
            foodAdapter.submitList(items)
        }
    }

    private fun setupCategoryChips() {
        val categories = listOf("All", "Burger", "Chicken", "Sides", "Coffee", "Bakery")
        binding.chipGroupMenu.removeAllViews()
        categories.forEach { cat ->
            val chip = Chip(context).apply {
                text = cat
                isCheckable = true
                isChecked = cat == "All"
            }
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) menuViewModel.filterByCategory(cat)
            }
            binding.chipGroupMenu.addView(chip)
        }
    }

    private fun setupFab() {
        binding.fabCart.setOnClickListener {
            findNavController().navigate(
                com.umai.foodnest.R.id.action_menu_to_cart
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
