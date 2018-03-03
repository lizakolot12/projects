package proj.kolot.uzsearch.moxy

import android.util.Log
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.ViewCommand
import com.arellomobile.mvp.viewstate.strategy.StateStrategy

/**
 * Created by Kolot Liza on 2/1/18.
 */
class AddToEndAndRemoveById : StateStrategy {

    override fun <View : MvpView> beforeApply(
            currentState: MutableList<ViewCommand<View>>,
            incomingCommand: ViewCommand<View>) {
                Log.e("my test", " begin size current state = " + currentState.size)
        val iterator = currentState.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.tag == incomingCommand.tag) {
                var idRemCom = incomingCommand.javaClass.getField("p0").get(incomingCommand)
                var idAddCom = entry.javaClass.getField("p0").get(entry)
                if (idAddCom == idRemCom) {
                    iterator.remove()
                }
            }
        }
        Log.e("my test", " end size current state = " + currentState.size)
        //currentState.add(incomingCommand)

    }

    override fun <View : MvpView> afterApply(
            currentState: MutableList<ViewCommand<View>>,
            incomingCommand: ViewCommand<View>) {
        //Just do nothing
        //currentState.remove(incomingCommand)
    }
}
