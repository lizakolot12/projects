package proj.kolot.uzsearch.list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.data.TransportRoute


@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ListTrainView : MvpView {
    //@StateStrategyType(value = SkipStrategy::class)
    fun showProgress()

    //@StateStrategyType(value = SkipStrategy::class)
    fun hideProgress()

    fun onTrainsLoaded(trains: List<TransportRoute>)
    fun showRouteName(name:String)
    fun showErrorMessage(msg: String)
    fun showErrorMessage(err: Error)
    fun hideErrorMessage()
}