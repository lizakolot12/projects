package proj.kolot.uzsearch.task.edit;

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.data.SeatFilter
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.moxy.AddToEndAndRemoveById


@StateStrategyType(value = AddToEndSingleStrategy::class)
interface EditTaskView : MvpView {

    fun showErrorInputData(list: List<EditTaskPresenter.Companion.Errors>)
    fun hideErrorInputData(tag:String)
    fun setInitialDate()
    fun setInitialTime()
    fun setInitialStationFrom()
    fun setInitialStationTo()
    fun setInitialPeriodicCheck()
    fun setInitialPeriod()
    fun setInitialFilterTrainsNumber()
    fun setInitialSettings(task: Task?)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showAllTask()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showResult(id:Int)


    @StateStrategyType(value = AddToEndStrategy::class, tag = "filters")
    fun addLineFilterSeat(id: Int, seatFilter: SeatFilter)

    @StateStrategyType(value = AddToEndAndRemoveById::class, tag = "filters")
    fun removeLineFilterSeat(id: Int)






}
