package com.creativeitinstitute.connection.views.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.creativeitinstitute.connection.R
import com.creativeitinstitute.connection.databinding.FragmentRegisterBinding
import com.creativeitinstitute.connection.nodes.DBNODES
import com.creativeitinstitute.connection.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var userDb: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        userDb = FirebaseDatabase.getInstance().reference

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerBtn.setOnClickListener {

            val name = binding.userName.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()


            if (isEmailValid(email) && isPasswordValid(password)) {

                registerUser(name, email, password)
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG)
                    .show()
            }

        }

        return binding.root
    }

    private fun registerUser(name: String, email: String, password: String) {

        var jAuth = FirebaseAuth.getInstance()
        jAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                saveUserToDataBase(jAuth.currentUser?.uid, email, name)


            } else {
                Toast.makeText(
                    context,
                    "Registration Failed : ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun saveUserToDataBase(uid: String?, email: String, name: String) {

        uid?.let {
            val user = User(
                userId = uid, email = email, fullName = name
            )


            userDb.child(DBNODES.USER).child(it).setValue(user).addOnCompleteListener { task ->


                if (task.isSuccessful) {

                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                } else {

                    Toast.makeText(
                        requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT
                    ).show()


                }


            }
        }
    }
}


    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"

        return password.isNotEmpty() && password.matches(Regex(passwordPattern)) // .length >=6
    }



