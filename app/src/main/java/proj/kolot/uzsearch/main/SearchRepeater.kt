package proj.kolot.uzsearch.main

import android.app.AlarmManager

/**
 * Created by Kolot Liza on 10/16/17.
 */
interface SearchRepeater {
    companion object {
        const val DEFAULT_PERIOD_REPEATING = AlarmManager.INTERVAL_DAY
    }
    fun runRepeatingTask(id:Int, on:Boolean, repeatingInterval:Long)
}