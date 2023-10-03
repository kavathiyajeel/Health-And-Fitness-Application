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
import com.example.health_and_fitness.Step_Counter.Model.ProgressState
import com.example.health_and_fitness.Step_Counter.Service.ProgressViewModel
import com.example.health_and_fitness.databinding.FragmentStepCounterBinding
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class Step_Counter_Frag : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels { ProgressViewModel }

    private var _binding: FragmentStepCounterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_step__counter, container, false)
        _binding = FragmentStepCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progress.collect { progress -> updateUserInterface(progress) }
            }
        }
    }

    private fun updateUserInterface(state: ProgressState) {
        updateProgress(state)
        //updateTree(state)
        updateTiles(state)
    }

    private fun updateProgress(state: ProgressState) = state.apply {
        val numberFormat = DecimalFormat.getIntegerInstance()
        val formattedStepCount = numberFormat.format(stepsTaken)
        val dailyGoalStepCount = numberFormat.format(dailyGoal)
        val dailyGoalText = getString(R.string.step_goal, dailyGoalStepCount)
        binding.apply {
            txtStepCount.text = formattedStepCount
            txtDailyGoal.text = dailyGoalText
            progressDailyGoal.max = dailyGoal
            progressDailyGoal.progress = stepsTaken
        }
    }

    private fun updateTiles(state: ProgressState) = state.apply {
        val calorieText = getString(
            R.string.calorie_burned_format, calorieBurned
        )
        val distanceText = getString(
            R.string.distance_travelled_format, distanceTravelled
        )

        binding.apply {
            txtCalorieBurned.text = calorieText
            txtDistanceTravelled.text = distanceText
        }
    }
}