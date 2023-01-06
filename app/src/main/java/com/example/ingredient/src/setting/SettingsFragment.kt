package com.example.ingredient.src.setting

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.ingredient.R

class SettingsFragment : PreferenceFragmentCompat() {
	lateinit var prefs: SharedPreferences

	// onCreate() 중에 호출되어 Fragment에 preference를 제공하는 메서드
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		// preference xml을 inflate하는 메서드
		setPreferencesFromResource(R.xml.root_preferences, rootKey)

		prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
	}

	override fun onResume() {
		super.onResume()
		prefs.registerOnSharedPreferenceChangeListener(listener)
	}

	override fun onPause() {
		super.onPause()
		prefs.unregisterOnSharedPreferenceChangeListener(listener)
	}
	var listener: SharedPreferences.OnSharedPreferenceChangeListener =
		SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
			when(key) {
				"${R.string.notice}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
				"${R.string.profileedit}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
				"${R.string.notify}" -> {
					Log.d("SettingsFragment", "set : ${sharedPreferences.getBoolean(key, false)}")
				}
				"${R.string.fontsize}" -> {
					Log.d("SettingsFragment", "set : ${sharedPreferences.getString(key, "small")}")
				}
				"${R.string.privacypolicy}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
				"${R.string.inquiry}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
				"${R.string.faq}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
				"${R.string.secession}" -> {
					Log.d("SettingsFragment", "set : $key")
				}
			}
		}

	companion object {
		@JvmStatic
		fun newInstance(act : Activity) = SettingsFragment()
	}
}