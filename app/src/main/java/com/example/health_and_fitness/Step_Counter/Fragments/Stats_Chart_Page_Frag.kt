package com.example.health_and_fitness.Step_Counter.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Service.ChartAdapter
import com.example.health_and_fitness.Step_Counter.Service.StatsChartPageViewModel
import com.example.health_and_fitness.Step_Counter.Service.StatsDetailsViewModel
import com.example.health_and_fitness.Step_Counter.Service.toChartValues
import com.example.health_and_fitness.databinding.FragmentStatsChartPageBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class Stats_Chart_Page_Frag : Fragment() {


    companion object {
        const val ARG_PAGE_NUMBER = "__page_number"
    }

    private lateinit var binding: FragmentStatsChartPageBinding
private val statsChartPageViewModel: StatsChartPageViewModel by viewModels { StatsChartPageViewModel.Factory }
    private val statsDetailsViewModel: StatsDetailsViewModel by activityViewModels { StatsDetailsViewModel.Factory }

    private var pageNumber: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    private val chartAdapter = ChartAdapter {
        statsDetailsViewModel.selectDay(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments?.getLong(ARG_PAGE_NUMBER) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_stats__chart__page, container, false)
        binding = FragmentStatsChartPageBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewChart.apply {
            adapter = chartAdapter
        }

        lifecycleScope.launch {
            val activeDayFlow = statsDetailsViewModel.day
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    val weekFlow = statsChartPageViewModel.week
                    weekFlow.combine(activeDayFlow) { week, activeDay ->
                        updateUserInterface(week, activeDay.date)
                    }.collect {}
                }
                launch {
                    activeDayFlow.collect {
                        updateSelectedWeek(it.chartDateRange.endInclusive)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInterface(week: List<Day>, activeDate: LocalDate) {
        val highestChartValue = week.maxOfOrNull { Integer.max(it.steps, it.goal) } ?: 1
        val locale = resources.configuration.locales[0]
        val chartValues = week.toChartValues(highestChartValue, locale, activeDate)
        chartAdapter.submitList(chartValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateSelectedWeek(lastDate: LocalDate) {
        val daysToSubtract = 7 * pageNumber + 6
        val firstDate = lastDate.minusDays(daysToSubtract)
        statsChartPageViewModel.selectWeek(firstDate)
    }
}