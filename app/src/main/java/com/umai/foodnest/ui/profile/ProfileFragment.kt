package com.umai.foodnest.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentProfileBinding
import com.umai.foodnest.ui.main.MainActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Glide.with(this).load(it).circleCrop().into(binding.ivProfile)
            Toast.makeText(context, "Profile photo updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName
            ?: user?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }
            ?: "User"

        binding.tvUserName.text = displayName
        binding.tvUserEmail.text = user?.email ?: ""

        // Load profile photo if exists
        user?.photoUrl?.let { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(binding.ivProfile)
        }

        // Tap profile photo to change it
        binding.ivProfile.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.menuAddresses.setOnClickListener {
            Toast.makeText(context, "📍 My Addresses", Toast.LENGTH_SHORT).show()
        }
        binding.menuPayment.setOnClickListener {
            Toast.makeText(context, "💳 Payment Methods", Toast.LENGTH_SHORT).show()
        }
        binding.menuNotifications.setOnClickListener {
            Toast.makeText(context, "🔔 Notifications", Toast.LENGTH_SHORT).show()
        }
        binding.menuHelp.setOnClickListener {
            Toast.makeText(context, "❓ Help & Support", Toast.LENGTH_SHORT).show()
        }
        binding.menuPrivacy.setOnClickListener {
            Toast.makeText(context, "🔒 Privacy Policy", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(),
                com.umai.foodnest.ui.main.MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}