package hr.cizmic.kalkulatorcizmic.activities

import android.app.Application

class ApplicationKalkulator : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: Application? = null
        fun instance(): Application? {
            return instance
        }
    }
}