package proj.kolot.uzsearch.main


class SearchRepeaterImpl: SearchRepeater {
    /* fun setListenerRepeating(repeatingTaskListener: RepeatingTaskListener) {
        mRepeatingTaskListener = repeatingTaskListener
    }

    var mRepeatingTaskListener: RepeatingTaskListener? = null*/
    @javax.inject.Inject
    lateinit var mContext: android.content.Context
   init {
        proj.kolot.uzsearch.MainApplication.Companion.graph.inject(this)
    }




    override fun runRepeatingTask(on:Boolean, repeatingInterval:Long){
        var service: proj.kolot.uzsearch.service.SearchService = proj.kolot.uzsearch.service.SearchService()
        service.setServiceAlarm(mContext, on, repeatingInterval)
    }


}