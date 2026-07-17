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
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.databinding.FragmentHomeBinding
import com.umai.foodnest.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RestaurantAdapter

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

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationsFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = RestaurantAdapter { restaurant ->
            val bundle = Bundle().apply { putInt("restaurantId", restaurant.id) }
            findNavController().navigate(R.id.action_home_to_menu, bundle)
        }
        binding.rvRestaurants.layoutManager = LinearLayoutManager(context)
        binding.rvRestaurants.adapter = adapter
    }

    private fun setupChips() {
        SampleData.categories.forEach { (name, emoji) ->
            val chip = Chip(context).apply {
                text = "$emoji $name"
                isCheckable = true
                isChecked = name == "All"
                chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                    if (name == "All") requireContext().getColor(R.color.orange_primary)
                    else requireContext().getColor(R.color.white)
                )
                setTextColor(
                    if (name == "All") requireContext().getColor(R.color.white)
                    else requireContext().getColor(R.color.text_primary)
                )
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.orange_primary))
                    chip.setTextColor(requireContext().getColor(R.color.white))
                    viewModel.loadRestaurants(category = name)
                } else {
                    chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.white))
                    chip.setTextColor(requireContext().getColor(R.color.text_primary))
                }
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

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}