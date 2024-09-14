package com.example.retrofitdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.retrofitdemo.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private var id: Int? = null
    private var username: String = ""
    private var email: String = ""
    private var password: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        getCurrentUser()

        binding.logoutButton.setOnClickListener {
            Utils.removeToken(requireContext())
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }

        binding.updateProfileButton.setOnClickListener {
            if (id != null) {
                val action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(
                    id = id!!,
                    username = username,
                    email = email,
                    password = password
                )
                navController.navigate(action)
            } else {
                Toast.makeText(requireContext(), "User data not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentUser()
    }

    private fun getCurrentUser() {
        val token = Utils.getToken(requireContext())

        if (token != null) {
            val authHeader = "Bearer $token"

            RetrofitClient.apiService.getCurrentUser(authHeader).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            id = user.id
                            username = user.username
                            email = user.email
                            password = user.password
                            binding.profileUsername.text = user.username
                            binding.profileEmail.text = user.email
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch user", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "No authentication token found", Toast.LENGTH_SHORT)
                .show()
        }
    }

}