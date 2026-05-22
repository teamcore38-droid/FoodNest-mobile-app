package com.umai.foodnest.ui.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.databinding.FragmentOffersBinding

class OffersFragment : Fragment() {
    private var _binding: FragmentOffersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OffersAdapter(SampleData.offers)
        binding.rvOffers.layoutManager = LinearLayoutManager(context)
        binding.rvOffers.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}