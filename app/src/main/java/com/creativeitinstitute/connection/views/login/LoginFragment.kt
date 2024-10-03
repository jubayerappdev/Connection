package com.creativeitinstitute.connection.views.login

import android.os.Binder
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.creativeitinstitute.connection.R
import com.creativeitinstitute.connection.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater, container, false)


        FirebaseAuth.getInstance().currentUser?.let {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.userEmail.text.toString().trim()
            val password = binding.password.text.toString().trim()


            if (isEmailValid(email) && isPasswordValid(password)){

                loginUser(email, password)
            }else{
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG).show()
            }




        }
        binding.registerBtn.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    private fun loginUser(email: String, password: String) {

        val jAuth = FirebaseAuth.getInstance()


        jAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->

            if (task.isSuccessful){

                val user = jAuth.currentUser
                Toast.makeText(context, "Login Successful : ${user?.email}", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }else{
                Toast.makeText(context, "Login Failed : ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }



    }

    fun isEmailValid(email: String): Boolean{
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String):Boolean{

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"

        return password.isNotEmpty() && password.matches(Regex(passwordPattern)) // .length >=6
    }


}