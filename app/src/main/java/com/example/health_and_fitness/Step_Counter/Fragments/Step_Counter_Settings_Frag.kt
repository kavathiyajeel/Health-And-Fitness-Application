package com.example.health_and_fitness.Step_Counter.Fragments

import android.os.Build
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Settings.SettingsViewModel


class Step_Counter_Settings_Frag : PreferenceFragmentCompat() {


    private val viewModel: SettingsViewModel by activityViewModels { SettingsViewModel }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observeSettingsChanges()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dailyGoalPreference = preferenceManager.findPreference<EditTextPreference>("daily_goal")
        dailyGoalPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
            val dailyGoal = it.text?.toIntOrNull() ?: 0
            resources.getQuantityString(R.plurals.daily_goal_summary, dailyGoal, dailyGoal)
        }

        val numericPreferenceKeys = listOf("daily_goal", "step_length", "height", "weight")
        numericPreferenceKeys.forEach {
            val preference = preferenceManager.findPreference<EditTextPreference>(it)
            preference?.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }


}