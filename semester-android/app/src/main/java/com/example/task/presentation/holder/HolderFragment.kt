package com.example.task.presentation.holder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentHolderBinding

class HolderFragment : Fragment(R.layout.fragment_holder) {

    private val binding : FragmentHolderBinding by viewBinding(FragmentHolderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = childFragmentManager.findFragmentById(R.id.fragment_container)
                as NavHostFragment
        val controller = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNav, controller)
    }

}