package com.umai.foodnest.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Already logged in? Skip login screen
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_login_to_home)
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Enter email"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etPassword.error = "Min 6 characters"
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = "Please wait..."

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(context,
                        "Welcome back!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_to_home)
                }
                .addOnFailureListener {
                    // Account not found? Create it
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(context,
                                "Account created!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_login_to_home)
                        }
                        .addOnFailureListener { e ->
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "LOGIN"
                            Toast.makeText(context,
                                "Failed: ${e.message}",
                                Toast.LENGTH_LONG).show()
                        }
                }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}