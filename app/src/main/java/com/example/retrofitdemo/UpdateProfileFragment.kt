package com.example.retrofitdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.retrofitdemo.databinding.FragmentUpdateProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val args = UpdateProfileFragmentArgs.fromBundle(requireArguments())
        val id = args.id
        val username = args.username
        val email = args.email
        val password = args.password

        binding.updateUsernameEditText.setText(username)
        binding.updateEmailEditText.setText(email)
        binding.updatePasswordEditText.setText(password)

        binding.saveButton.setOnClickListener {
//            updateUser(id, username, email, password)
            val updatedUsername = binding.updateUsernameEditText.text.toString()
            val updatedEmail = binding.updateEmailEditText.text.toString()
            val updatedPassword = binding.updatePasswordEditText.text.toString()
            updateUser(id, updatedUsername, updatedEmail, updatedPassword)
        }
    }

    private fun updateUser(id: Int, updatedUsername: String?, updatedEmail: String?, updatedPassword: String?) {
        val userUpdateRequest = UserUpdateRequest(updatedUsername, updatedEmail, updatedPassword)
        val token = Utils.getToken(requireContext())
        val authHeader = "Bearer $token"

        RetrofitClient.apiService.updateUser(id, authHeader, userUpdateRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(UpdateProfileFragmentDirections.actionUpdateProfileFragmentToProfileFragment())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(context, "Update failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}