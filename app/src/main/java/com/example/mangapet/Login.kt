package com.example.mangapet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mangapet.databinding.FragmentLoginBinding



class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.inflate(layoutInflater)
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

        val myViewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        binding.loginButton.setOnClickListener {
            myViewModel.signInUser(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString()
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
                                "Failed to sign in user! ${it.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else->{}
                }
            }
        }

        binding.registerTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, Registration())
                .commit()
        }


    }





}