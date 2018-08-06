package proj.kolot.uzsearch.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.SeatType
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.route.RouteActivity
import java.util.*
import javax.inject.Inject

class NotificationFactory {
    companion object {
        const val DEFAULT_NEED_NOTIFICATION = true
        val ID_SMALL_ICON = R.drawable.ic_train_white_18dp
        val VIBRATE_PATTERN: LongArray
            get() = longArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000)
    }

    @Inject
    lateinit var context: Context
    private val prefs: SharedPreferences

    init {
        MainApplication.graph.inject(this)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)

    }

    fun createNotification(trains: List<TransportRoute>, task: Task?): Notification {
        val notifyBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        notifyBuilder.setTicker(getTicker())
                .setSmallIcon(ID_SMALL_ICON)
                .setContentTitle(getContentTitle(task))
                .setContentText(getContentText(trains))
                .setContentIntent(getPendingIntent(task?.id ?: 0))
                .setAutoCancel(true)
                .setUsesChronometer(true)
                .setSound(getRingtoneUri())

        if (needVibrate()) notifyBuilder.setVibrate(VIBRATE_PATTERN)

        val notification: Notification = notifyBuilder.build()

        if (isImportant()) {
            notification.flags = Notification.FLAG_INSISTENT
        }

        return notification
    }

    private fun getTicker(): String {
        return context.getString(R.string.notification_title)
    }

    private fun getContentTitle(task: Task?): String {
        return context.getString(R.string.notification_title) + ":" + task?.stationFrom?.name + " -> " + task?.stationTo?.name
    }

    private fun getPendingIntent(id: Int): PendingIntent? {
        var intent: Intent = RouteActivity.newIntent(context, id)
        intent.data = Uri.parse(id.toString())

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(RouteActivity::class.java)
        stackBuilder.addNextIntent(intent)
        return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun needVibrate(): Boolean {
        return prefs.getBoolean(context.getString(R.string.pref_key_vibrate), DEFAULT_NEED_NOTIFICATION)
    }

    private fun isImportant(): Boolean {
        return prefs.getBoolean(context.getString(R.string.pref_key_duration), false)
    }

    private fun getTypeSound(): String {
        return prefs.getString(context.getString(R.string.pref_key_type_ringtone), "")
    }

    private fun getRingtoneUri(): Uri {
        return when (getTypeSound()) {
            context.getString(R.string.pref_type_sound_ring) -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            context.getString(R.string.pref_type_sound_notification) -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            context.getString(R.string.pref_type_sound_alarm) -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            context.getString(R.string.pref_type_sound_melody) -> Uri.parse(prefs.getString(context.getString(R.string.pref_key_uri_ringtone), ""))
            else -> {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
        }
    }

    private fun getContentText(trains: List<TransportRoute>): String {
        var content = ""
        var row: String
        for (route: TransportRoute in trains) {
            row = route.id + " " + route.name + " "
            for (entry: Map.Entry<SeatType, Int> in route.freeSeatsCountByType
                    ?: Collections.emptyMap()) {
                row += entry.key.id + ":" + entry.value + ", "
            }
            row += "\n"
            content += row
        }
        return content
    }
}