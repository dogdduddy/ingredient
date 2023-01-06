package com.example.ingredient.src.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ingredient.R

class SettingActivity : AppCompatActivity() {
	private lateinit var settingFragment: SettingsFragment
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_setting)

		settingFragment = SettingsFragment.newInstance(this)

		supportFragmentManager
			.beginTransaction()
			.replace(R.id.fragment_container, settingFragment)
			.commit()
	}
}