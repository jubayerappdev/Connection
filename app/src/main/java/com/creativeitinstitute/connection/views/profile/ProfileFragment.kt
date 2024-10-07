package com.creativeitinstitute.connection.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creativeitinstitute.connection.databinding.FragmentProfileBinding
import com.creativeitinstitute.connection.nodes.DBNODES
import com.creativeitinstitute.connection.utils.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var userDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container,false)
        userDB= FirebaseDatabase.getInstance().reference
        requireArguments().getString(USERID)?.let {
            getUserByID(it)
        }
        return binding.root
    }

    companion object{
        const val USERID = "user_id_key"
    }

    private fun getUserByID(userId: String) {

        userDB.child(DBNODES.USER).child(userId).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(User::class.java)?.let {
                        binding.userEmail.text = it.email
                        binding.bio.text = it.bio
                        binding.fullName.text = it.fullName
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

    }




}