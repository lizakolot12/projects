package proj.kolot.uzsearch.moxy

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
        val iterator = currentState.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.tag == incomingCommand.tag) {
                var idRemCom = incomingCommand.javaClass.getField("id").get(incomingCommand)//incomingCommand.javaClass.getField("p0").get(incomingCommand)
                var idAddCom = entry.javaClass.getField("id").get(entry)
                if (idAddCom == idRemCom) {
                    iterator.remove()
                }
            }
        }


    }

    override fun <View : MvpView> afterApply(
            currentState: MutableList<ViewCommand<View>>,
            incomingCommand: ViewCommand<View>) {
        //Just do nothing
        //currentState.remove(incomingCommand)
    }
}
