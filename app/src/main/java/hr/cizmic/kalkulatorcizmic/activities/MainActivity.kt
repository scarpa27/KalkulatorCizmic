package hr.cizmic.kalkulatorcizmic.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import hr.cizmic.kalkulatorcizmic.R
import hr.cizmic.kalkulatorcizmic.databinding.ActivityMainBinding
import hr.cizmic.kalkulatorcizmic.utils.Util

class MainActivity : AppCompatActivity() {
    private lateinit var lang_short: String
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lang_short = Util.getLang();
        Util.setLocale(this, lang_short)
        Util.onActivityCreateSetTheme(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setLangSpinnerAdapter()
        setDarkModeToggle()
        setCommonListeners()

    }

    private fun setLangSpinnerAdapter() {
        val spinner: Spinner = binding.spinnerLanguages
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.langs,
            android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(Util.langShort2Index(lang_short))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (Util.langShort2Index(Util.getLang()) != p2) {
                    Util.setLang(Util.langIndex2Short(p2))
                    Util.setLocale(getMe(), Util.langIndex2Short(p2))
                    finish()
                    startActivity(intent)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setCommonListeners() {


        //start calculator on upper bottun click
        binding.btnStartCalc.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }
    }

    private fun setDarkModeToggle() {
        val pref = getSharedPreferences(R.string.app_package.toString(), Context.MODE_PRIVATE)
        val dark = pref.getBoolean("dark_mode", false)
        binding.switchDarkMode.isChecked = dark
        binding.switchDarkMode.setOnCheckedChangeListener { _, b ->
            pref.edit().putBoolean("dark_mode", b).apply()
            Util.changeLocalTheme(b)
            finish()
            startActivity(intent)
            Log.d("banka", "darkmode toggled "+ pref.getBoolean("dark_mode", false).toString())
        }
    }

    private fun getMe() = this

}