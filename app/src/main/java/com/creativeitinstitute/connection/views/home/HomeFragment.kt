package com.creativeitinstitute.connection.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.creativeitinstitute.connection.R
import com.creativeitinstitute.connection.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container,false)


        binding.btnLogout.setOnClickListener {

            var jAuth = FirebaseAuth.getInstance()

            jAuth.signOut().apply {

                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }

        }



        return binding.root


    }


}