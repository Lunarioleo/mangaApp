package com.example.mangapet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangapet.databinding.FragmentUsersListBinding


class UsersList : Fragment() {
    private lateinit var binding: FragmentUsersListBinding
    private val adapter = UsersListAdapter()
    private lateinit var  myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUsersListBinding.inflate(layoutInflater)
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

        myViewModel.getUsersList()

        binding.rcViewUsersList.layoutManager = LinearLayoutManager(requireContext())
        binding.rcViewUsersList.adapter = adapter


        myViewModel.uiState.observe(viewLifecycleOwner){
            when (it){
                is MyViewModel.UiStates.UsersInfoList->{
                    adapter.updateUserList(it.usersList)
                }
                else ->{}
            }
        }


        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


}