package hr.cizmic.kalkulatorcizmic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import hr.cizmic.kalkulatorcizmic.R
import hr.cizmic.kalkulatorcizmic.activities.ApplicationKalkulator
import java.util.*

object Util {
    private const val THEME_LIGHT = 0
    private const val THEME_DARK = 1
    private var curTheme = 0

    fun changeTheme(activity: Activity, theme: Int) {
        curTheme = theme
        activity.finish()
        activity.startActivity(Intent(activity, activity.javaClass))
    }

    fun changeLocalTheme(boolean: Boolean) {
        curTheme =
            if (boolean)
                THEME_DARK
            else
                THEME_LIGHT
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        changeLocalTheme(isDarkTheme())
        when (curTheme) {
            THEME_LIGHT -> activity.setTheme(R.style.StyleLight)
            THEME_DARK -> activity.setTheme(R.style.StyleDark)
        }
    }

    fun isDarkTheme(): Boolean {
        val pref = ApplicationKalkulator.instance()!!
            .getSharedPreferences(R.string.app_package.toString(), Context.MODE_PRIVATE)
        return pref.getBoolean("dark_mode", false)
    }


    fun setLocale(activity: Activity, lang: String) {
        val mLocale = Locale(lang)
        val res: Resources = activity.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(mLocale)
        res.updateConfiguration(conf, dm)
    }

    fun getLang(): String {
        val pref = ApplicationKalkulator.instance()!!
            .getSharedPreferences(R.string.app_package.toString(), Context.MODE_PRIVATE)
        val info: String = pref.getString("lang", "en").toString()
        Log.d("banka", info)
        return info
    }

    fun setLang(lang: String) {
        val pref = ApplicationKalkulator.instance()!!
            .getSharedPreferences(R.string.app_package.toString(), Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("lang", lang)
        edit.apply()
    }

    fun langShort2Index(lang: String): Int =
        when (lang) {
            "en" -> 0
            "hr" -> 1
            "de" -> 2
            else -> 0
        }

    fun langIndex2Short(lang: Int): String =
        when (lang) {
            0 -> "en"
            1 -> "hr"
            2 -> "de"
            else -> "en"
        }
}