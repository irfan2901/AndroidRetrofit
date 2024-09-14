package com.example.retrofitdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.retrofitdemo.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.registerButton.setOnClickListener {
            val username = binding.etregisterUsername.text.toString()
            val email = binding.etregisterEmail.text.toString().trim()
            val password = binding.etregisterPassword.text.toString().trim()

            binding.registerUsernameError.error = null
            binding.registerEmailError.error = null
            binding.registerPasswordError.error = null

            var isValid = true

            if (username.isEmpty()) {
                binding.registerUsernameError.error = "Username is required"
                isValid = false
            }

            if (email.isEmpty()) {
                binding.registerEmailError.error = "Email is required"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.registerEmailError.error = "Invalid email format"
                isValid = false
            }

            if (password.isEmpty()) {
                binding.registerPasswordError.error = "Password is required"
                isValid = false
            }

            if (isValid) {
                registerUser(username, email, password)
            }
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val user = User(username = username, email = email, password = password)
        RetrofitClient.apiService.registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        Utils.saveToken(requireContext(), registerResponse.token)
                        Toast.makeText(requireContext(), "Register successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToProfileFragment())
                    } else {
                        Toast.makeText(requireContext(), "Unexpected response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Register failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}