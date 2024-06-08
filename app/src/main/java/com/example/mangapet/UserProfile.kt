package com.example.mangapet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mangapet.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class UserProfile : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var  myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        myViewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
//        myViewModel.retrieveImageFromDatabase()
        myViewModel.getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.uiState.observe(viewLifecycleOwner){
            when (it){
                is MyViewModel.UiStates.UserFullInfo->{
                    binding.profileUserName.text = it.userName
                    if (it.profileAvatar != null) {
                        Picasso.get()
                            .load(it.profileAvatar)
                            .into(binding.profileAvatar)
                    }
                }
                else->{}
            }
        }
        binding.buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, Login())
                    .commit()
        }

        binding.buttonUserSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, UserSettings())
                .addToBackStack("UserProfile")
                .commit()
        }
    }


}