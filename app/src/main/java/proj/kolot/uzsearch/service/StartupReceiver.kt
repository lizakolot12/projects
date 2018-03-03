package proj.kolot.uzsearch.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.settings.SettingsStorage

/**
 * Created by Kolot Liza on 11/29/17.
 */
class StartupReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("my test","sturtupreceiver")
        var settingsStorage: SettingsStorage =  SettingsStorage()
        var repeater: SearchRepeater = SearchRepeaterImpl()
        repeater.runRepeatingTask(settingsStorage.needPeriodically(), settingsStorage.getPeriod())
    }
}