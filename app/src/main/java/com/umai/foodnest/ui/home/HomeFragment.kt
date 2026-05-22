package com.umai.foodnest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentHomeBinding
import com.umai.foodnest.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RestaurantAdapter

    private val categories = listOf("All", "Fast Food", "Pizza", "Cafe", "Sandwiches")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChips()
        setupSearch()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = RestaurantAdapter { restaurant ->
            val bundle = android.os.Bundle().apply {
                putInt("restaurantId", restaurant.id)
            }
            findNavController().navigate(R.id.action_home_to_menu, bundle)
        }
        binding.rvRestaurants.layoutManager = LinearLayoutManager(context)
        binding.rvRestaurants.adapter = adapter
    }

    private fun setupChips() {
        categories.forEach { cat ->
            val chip = Chip(context).apply {
                text = cat
                isCheckable = true
                isChecked = cat == "All"
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.loadRestaurants(category = cat)
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.loadRestaurants(query = text.toString())
        }
    }

    private fun observeData() {
        viewModel.restaurants.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}