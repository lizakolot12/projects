package proj.kolot.uzsearch.list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proj.kolot.uzsearch.settings.SettingsStorage

/**
 * Created by Kolot Liza on 6/6/18.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TasksView : MvpView {
    fun  showTasks(settings:List<SettingsStorage.Settings>)
    fun  openDetail(id:Int)
}