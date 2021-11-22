package com.example.simsapp.ui.main

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simsapp.databinding.MainFragmentBinding


class MainFragment : Fragment() {



    companion object {
        fun newInstance() = MainFragment()
        const val PERMISSION_READ_STATE : Int = 1
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding : MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner= this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.viewmodel = viewModel
        binding.fragment = this

        if(context?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),  arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_STATE)
        }
        if(context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_READ_STATE)
        }

    }

    fun moveToList(){
        val navController = findNavController()
        val action = MainFragmentDirections.actionMainFragmentToSubscriptionFragment()
        navController.navigate(action)
    }

    fun moveToCellInfoList(){
        viewModel.listInfo()
        val navController = findNavController()
        val action = MainFragmentDirections.actionMainFragmentToCellInfoFragment()
        navController.navigate(action)
    }



}