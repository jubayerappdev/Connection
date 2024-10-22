package com.creativeitinstitute.connection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.creativeitinstitute.connection.databinding.FragmentEditProfileBinding
import com.creativeitinstitute.connection.databinding.FragmentProfileBinding
import com.creativeitinstitute.connection.nodes.DBNODES
import com.creativeitinstitute.connection.utils.User
import com.creativeitinstitute.connection.views.profile.ProfileFragment.Companion.USERID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditProfileFragment : Fragment() {

    lateinit var binding: FragmentEditProfileBinding

    lateinit var userDB: DatabaseReference



    private var userId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        userDB= FirebaseDatabase.getInstance().reference

        requireArguments().getString(USERID)?.let {
            userId=it

            getUserByID(it)
        }
        binding.saveUserBTn.setOnClickListener {
            var userMap:MutableMap<String, Any> = mutableMapOf()

            userMap["fullName"] = binding.fullName.text.toString().trim()
            userMap["bio"] = binding.bio.text.toString().trim()

            userDB.child(DBNODES.USER).child(userId).updateChildren(userMap).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Profile Updated! ", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),"${it.exception?.message} ", Toast.LENGTH_LONG).show()
                }
            }

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
                        binding.bio.setText(it.bio)
                        binding.fullName.setText(it.fullName)
                    }

                }
                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

    }


}