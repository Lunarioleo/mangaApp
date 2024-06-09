package com.example.mangapet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangapet.databinding.FragmentUserSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso


class UserSettings : Fragment() {

    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var  myViewModel: MyViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserSettingsBinding.inflate(layoutInflater)
        myViewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
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
        myViewModel.getCurrentUserInfo()



        myViewModel.uiState.observe(viewLifecycleOwner){
            when (it){
                is MyViewModel.UiStates.UserFullInfo->{
                        Picasso.get()
                            .load(it.profileAvatar)
                            .into(binding.settingsAvatar2)
                    binding.userName.text = it.userName
                }
                else->{}
            }
        }


        binding.settingsAvatar.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.buttonChangeName.setOnClickListener {
            myViewModel.changeUserName(binding.changeUserName.text.toString().trim())
        }
    }




    private val changeImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val imageUri = data?.data
            if (imageUri != null){
                myViewModel.saveImageToStorage(imageUri)
            }
        }
    }

}