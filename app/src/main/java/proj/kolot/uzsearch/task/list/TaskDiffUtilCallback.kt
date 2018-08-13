package proj.kolot.uzsearch.task.list

import android.support.v7.util.DiffUtil
import proj.kolot.uzsearch.data.Task



/**
 * Created by Kolot Liza on 8/7/18.
 */
class TaskDiffUtilCallback(private var oldList: List<Task>, private var newList: List<Task>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        val result = oldTask.stationFrom?.equals(newTask.stationFrom)?:false&&
                oldTask.stationTo?.equals(newTask.stationTo)?:false&&
                oldTask.numberTrain?.equals(newTask.numberTrain)?:false&&
                oldTask.dateRoute?.equals(newTask.dateRoute)?:false &&
                oldTask.needPeriodicCheck.equals(newTask.needPeriodicCheck)
                //&&oldTask.period?.equals(newTask.period)?:false&&
               // oldTask.seatFilters?.equals(newTask.seatFilters)?:false
        return result
    }
}