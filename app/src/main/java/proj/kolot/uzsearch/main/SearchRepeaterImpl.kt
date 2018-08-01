package proj.kolot.uzsearch.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.service.SearchService
import javax.inject.Inject


class SearchRepeaterImpl : SearchRepeater {

    @Inject
    lateinit var context: Context

    init {
        MainApplication.graph.inject(this)
    }
    companion object {
        val DEFAULT_PERIOD_REPEATING = AlarmManager.INTERVAL_DAY
    }

    override fun runRepeatingTask(id: Int, on: Boolean, repeatingInterval: Long) {
        var intent: Intent = SearchService.newIntent(context, id)
        intent.data = Uri.parse(id.toString())
        var pendingIntent: PendingIntent = PendingIntent.getService(context, id, intent, 0)
        var am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (on) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() ,//+ repeatingInterval,
                    repeatingInterval, pendingIntent)
        } else {
            am.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }


    fun isServiceAlarmOn(context: Context): Boolean {
        var intent: Intent = Intent(context, SearchService::class.java)
        var pendingIntent: PendingIntent? = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE)
        return pendingIntent != null
    }

}