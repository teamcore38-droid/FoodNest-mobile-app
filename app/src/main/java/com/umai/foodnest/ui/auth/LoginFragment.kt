package com.umai.foodnest.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umai.foodnest.R
import com.umai.foodnest.databinding.FragmentLoginBinding
import com.umai.foodnest.ui.admin.AdminActivity

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

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

        // Already logged in? Route based on role
        if (auth.currentUser != null) {
            routeByRole(auth.currentUser!!.uid)
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
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                    routeByRole(uid)
                }
                .addOnFailureListener {
                    // Account not found — create it
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: return@addOnSuccessListener
                            // New user: create profile with role=customer
                            db.collection("users").document(uid).set(mapOf(
                                "id"    to uid,
                                "email" to email,
                                "name"  to email.substringBefore("@"),
                                "role"  to "customer",
                                "phone" to "",
                                "profileImageUrl" to "",
                                "createdAt" to System.currentTimeMillis()
                            ))
                            Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_login_to_home)
                        }
                        .addOnFailureListener { e ->
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "LOGIN"
                            Toast.makeText(context,
                                "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    /** Check role in Firestore and launch the right screen */
    private fun routeByRole(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val role = doc.getString("role") ?: "customer"
                if (role == "admin") {
                    startActivity(Intent(requireContext(), AdminActivity::class.java))
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.action_login_to_home)
                }
            }
            .addOnFailureListener {
                // Fallback to customer app if Firestore unreachable
                findNavController().navigate(R.id.action_login_to_home)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}