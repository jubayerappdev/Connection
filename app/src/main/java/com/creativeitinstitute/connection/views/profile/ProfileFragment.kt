package com.creativeitinstitute.connection.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.creativeitinstitute.connection.EditProfileFragment
import com.creativeitinstitute.connection.R
import com.creativeitinstitute.connection.databinding.FragmentProfileBinding
import com.creativeitinstitute.connection.nodes.DBNODES
import com.creativeitinstitute.connection.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var userDB: DatabaseReference


    private var currentUser: FirebaseUser? = null

    private var userId = ""

    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container,false)
        userDB= FirebaseDatabase.getInstance().reference

        requireArguments().getString(USERID)?.let {
            userId=it

            getUserByID(it)
        }

        FirebaseAuth.getInstance().currentUser?.let {
            if (userId==it.uid){

                binding.chatWithUserBTn.text = EDIT

            }else{
                binding.chatWithUserBTn.text = CHAT
            }

            binding.chatWithUserBTn.setOnClickListener {

                bundle.putString(EditProfileFragment.USERID,userId)

                if (binding.chatWithUserBTn.text == EDIT){


                    findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment, bundle)
                }else{

                    findNavController().navigate(R.id.action_profileFragment_to_chatFragment,bundle)
                }
            }

        }
        return binding.root
    }

    companion object{
        const val USERID = "user_id_key"
        const val EDIT = "Edit Profile"
        const val CHAT = "Let's Chat"
    }

    private fun getUserByID(userId: String) {





        userDB.child(DBNODES.USER).child(userId).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(User::class.java)?.let {
                        binding.userEmail.text = it.email
                        binding.bio.text = it.bio
                        binding.fullName.text = it.fullName

                        binding.profileImage.load(it.profileImage)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

    }

}