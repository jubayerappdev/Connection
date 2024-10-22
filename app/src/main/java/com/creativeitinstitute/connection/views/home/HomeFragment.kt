package com.creativeitinstitute.connection.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.creativeitinstitute.connection.R
import com.creativeitinstitute.connection.databinding.FragmentHomeBinding
import com.creativeitinstitute.connection.nodes.DBNODES
import com.creativeitinstitute.connection.views.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.creativeitinstitute.connection.utils.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), UserAdapter.UserListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var userDB:DatabaseReference
    lateinit var adapter: UserAdapter

    val userList: MutableList<User> = mutableListOf()

    private val jAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseUser :FirebaseUser

    private var currentUser: User? = null

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container,false)
        userDB = FirebaseDatabase.getInstance().reference
        FirebaseAuth.getInstance().currentUser?.let {

            firebaseUser = it
        }

        binding.topBar.profileImage.setOnClickListener {
            currentUser?.let {
                bundle.putString(ProfileFragment.USERID,it.userId)

                findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)

            }


        }



        binding.topBar.logoutBtn.setOnClickListener {


            jAuth.signOut().apply {

                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }




        adapter = UserAdapter(this@HomeFragment)

        binding.userRcv.adapter = adapter

        getAllAvailableUser()






        return binding.root


    }

    private fun getAllAvailableUser() {

        userDB.child(DBNODES.USER).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    userList.clear()
                    snapshot.children.forEach {

                        val user: User = it.getValue(User::class.java)!!

                        if (firebaseUser.uid!=user.userId){
                            userList.add(user)
                        }else{
                            currentUser=user

                            setProfile()
                        }

                    }
                    adapter.submitList(userList)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )

    }

    private fun setProfile() {
        currentUser?.let {

            binding.topBar.profileImage.load("https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/8f2557cc-b525-4792-8f8c-87297f877ca0/width=450/00739.jpeg")

        }

    }

    override fun userItemClick(user: User) {


        bundle.putString(ProfileFragment.USERID,user.userId)

        findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)

    }


}