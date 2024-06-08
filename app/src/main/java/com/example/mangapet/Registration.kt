package com.example.mangapet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mangapet.databinding.FragmentRegistrationBinding


class Registration : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var myViewModel: MyViewModel






    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


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
        myViewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]


        val currentUser = myViewModel.getCurrentUser().currentUser

        if (currentUser != null){
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, UserProfile())
                .commit()
        }
        binding.registrationButton.setOnClickListener {
            myViewModel.createUser(
                binding.registerEmail.text.toString(),
                binding.registerPassword.text.toString()
            )
            myViewModel.uiState.observe(viewLifecycleOwner) {
                when (it) {
                    is MyViewModel.UiStates.UserState -> {
                        if (it.isUserRegistered) {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container, UserProfile())
                                .commit()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to register user! ${it.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else->{}
                }
            }
        }

        binding.signInView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, Login())
                .commit()
        }

    }

}