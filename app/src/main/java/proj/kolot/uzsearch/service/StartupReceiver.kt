package proj.kolot.uzsearch.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.storage.Storage
import javax.inject.Inject

/**
 * Created by Kolot Liza on 11/29/17.
 */
class StartupReceiver: BroadcastReceiver() {
    @Inject
    lateinit var requestStorage: Storage
    @Inject
    lateinit var repeater: SearchRepeater


    init {
        MainApplication.graph.inject(this)
    }
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("my test","sturtupreceiver")
        runAllTask()
    }

    private fun runAllTask() {
        val runnable = object : Runnable {
            override fun run() {
                synchronized(repeater) {
                    try {

                        var list = requestStorage.getRequestByNeedPeriodic(true)
                        list.forEach {

                            repeater.runRepeatingTask(it.id ?: -1, true, it.period ?: 0)
                        }
                    } catch (e: Exception) {
                        Log.e("my test", " exception in broadcast receiver")
                    }

                }
            }
        }
        val thread = Thread(runnable)
        thread.start()

    }

}