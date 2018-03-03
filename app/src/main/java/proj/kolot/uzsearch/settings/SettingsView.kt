package proj.kolot.uzsearch.settings;


import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.moxy.AddToEndAndRemoveById


@StateStrategyType(value = AddToEndSingleStrategy::class)
interface SettingsView : MvpView {

    fun showErrorInputData(list: List<Integer>)
    fun hideErrorInputData(tag:String)
    fun setInitialDate()
    fun setInitialStationFrom(station:Station)
    fun setInitialStationTo(station:Station)
    fun setInitialPeriodicCheck()
    fun setInitialPeriod()
    fun setInitialSettings(settings: SettingsStorage.Settings)


    @StateStrategyType(value = AddToEndStrategy::class, tag = "filters")
    fun addLineFilterSeat(id: Int)

    @StateStrategyType(value = AddToEndAndRemoveById::class, tag = "filters")
    fun removeLineFilterSeat(id: Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showResult()


}
