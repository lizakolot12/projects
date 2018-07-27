package proj.kolot.uzsearch.task.list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.data.Task

/**
 * Created by Kolot Liza on 6/6/18.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TasksView : MvpView {
    fun showTasks(settings: List<Task>)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun openDetail(id: Int)
}