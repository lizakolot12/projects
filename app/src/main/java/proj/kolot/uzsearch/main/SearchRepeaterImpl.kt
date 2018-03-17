package proj.kolot.uzsearch.main

import android.content.Context
import proj.kolot.uzsearch.MainApplication
import javax.inject.Inject


class SearchRepeaterImpl : SearchRepeater {

    @Inject
    lateinit var mContext: Context

    init {
        MainApplication.graph.inject(this)
    }


    override fun runRepeatingTask(on: Boolean, repeatingInterval: Long) {
        var service: proj.kolot.uzsearch.service.SearchService = proj.kolot.uzsearch.service.SearchService()
        service.setServiceAlarm(mContext, on, repeatingInterval)
    }


}