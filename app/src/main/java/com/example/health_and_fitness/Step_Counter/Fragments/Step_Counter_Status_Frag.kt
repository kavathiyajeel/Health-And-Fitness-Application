package com.example.health_and_fitness.Step_Counter.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Model.StatsDetailsState
import com.example.health_and_fitness.Step_Counter.Service.StatsDetailsViewModel
import com.example.health_and_fitness.databinding.FragmentStepCounterStatusBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class Step_Counter_Status_Frag : Fragment() {
    private val viewModel: StatsDetailsViewModel by activityViewModels { StatsDetailsViewModel }


    private lateinit var binding: FragmentStepCounterStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_step__counter__status, container, false)
        binding = FragmentStepCounterStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.day.collect { updateUserInterface(it) }
            }
        }
    }

    private fun updateUserInterface(state: StatsDetailsState) = state.apply {
        val stepsText =
            resources.getQuantityString(R.plurals.step_count_format, stepsTaken, stepsTaken)
        val calorieText = getString(R.string.calorie_burned_format, calorieBurned)
        val distanceText = getString(R.string.distance_travelled_format, distanceTravelled)

        binding.apply {
            txtStepCount.text = stepsText
            txtCalorieBurned.text = calorieText
            txtDistanceTravelled.text = distanceText
        }
    }

}