package com.example.health_and_fitness.Step_Counter.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Service.StatsDetailsViewModel
import com.example.health_and_fitness.databinding.FragmentStatsChartBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class Stats_Chart_Frag : Fragment() {
    private val statsDetailsViewModel: StatsDetailsViewModel by activityViewModels { StatsDetailsViewModel.Factory }

    private lateinit var binding: FragmentStatsChartBinding
    private lateinit var chartPageAdapter: ChartPageAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_stats__chart, container, false)
        binding = FragmentStatsChartBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartPageAdapter = ChartPageAdapter(this)
        binding.viewPagerChart.adapter = chartPageAdapter

        binding.buttonPreviousDay.setOnClickListener { changeSelectedDate(offset = -1) }
        binding.buttonNextDay.setOnClickListener { changeSelectedDate(offset = 1) }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsDetailsViewModel.day.collect {
                    updateUserInterface(it.date, it.chartDateRange)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeSelectedDate(offset: Long) {
        val currentDate = statsDetailsViewModel.day.value.date
        statsDetailsViewModel.selectDay(currentDate.plusDays(offset))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInterface(selectedDate: LocalDate, dateRange: ClosedRange<LocalDate>) {
        binding.apply {
            textSelectedDate.text = selectedDate.format(dateFormatter)
            buttonPreviousDay.isVisible = selectedDate.isAfter(dateRange.start)
            buttonNextDay.isVisible = selectedDate.isBefore(dateRange.endInclusive)
            chartPageAdapter.dateRange = dateRange
            scrollChartTo(selectedDate)
        }
    }

    private fun scrollChartTo(
        selectedDate: LocalDate,
    ) {
        val pageIndex = chartPageAdapter.getPageContaining(selectedDate)
        binding.viewPagerChart.currentItem = pageIndex
    }

    class ChartPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        @RequiresApi(Build.VERSION_CODES.O)
        var dateRange = LocalDate.now()..LocalDate.now()

        @RequiresApi(Build.VERSION_CODES.O)
        fun getPageContaining(selectedDate: LocalDate): Int {
            val period = Period.between(selectedDate, dateRange.endInclusive)
            return (period.days / 7).coerceIn(0, itemCount)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getItemCount(): Int = dateRange.run {
            val period = Period.between(start, endInclusive)
            return period.days / 7 + 1
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = Stats_Chart_Page_Frag()
            fragment.arguments = Bundle().apply {
                putLong(Stats_Chart_Page_Frag.ARG_PAGE_NUMBER, position.toLong())
            }
            return fragment
        }
    }
}