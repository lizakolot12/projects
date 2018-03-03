package proj.kolot.uzsearch.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.data.TransportRoute


@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TrainsCheckView: MvpView {
    fun showFoundTrains(trains:List<TransportRoute>)
    fun runRepeatingTask(on:Boolean, repeatingInterval:Long)
    fun setListenerRepeating(repeatingTaskListener:RepeatingTaskListener)
}

interface RepeatingTaskListener {
    fun onHandleTask()
}