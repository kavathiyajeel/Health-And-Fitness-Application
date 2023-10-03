package com.example.health_and_fitness.Step_Counter.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.FragmentRecognitionPermissionBinding


class RecognitionPermission_Frag : Fragment() {

    private var _binding: FragmentRecognitionPermissionBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        when (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACTIVITY_RECOGNITION
        )) {
            PackageManager.PERMISSION_GRANTED -> openMainActivity()
            PackageManager.PERMISSION_DENIED -> openPermissionSettings()
        }
    }
    private fun openMainActivity() {
        val action = R.id.action_RecognitionPermissionFragment_to_stepcounterActivity
        findNavController().navigate(action)
        requireActivity().finish()
    }

    private fun openPermissionSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireContext().packageName, null)
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_recognition_permission, container, false)
        _binding = FragmentRecognitionPermissionBinding.inflate(inflater, container, false)
        return binding.root
        // Hide the action bar in the fragment
        (activity as AppCompatActivity).supportActionBar?.hide()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRecognitionPermission.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
    }

}