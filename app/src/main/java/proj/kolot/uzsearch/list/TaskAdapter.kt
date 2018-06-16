package proj.kolot.uzsearch.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_task.view.*
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.settings.SettingsStorage
import java.util.*

/**
 * Created by Kolot Liza on 6/6/18.
 */
class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    companion object {
        // val LOCALE_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM HH:mm")
    }

    private var list: List<SettingsStorage.Settings> = ArrayList()
    private var listener: OnItemTaskClickListener? = null

    constructor(list: List<SettingsStorage.Settings>) {
        this.list = list
    }

    fun setOnItemTaskClickListener(listener:OnItemTaskClickListener){
        this.listener = listener
    }
    fun setList(list: List<SettingsStorage.Settings>) {
        this.list = list
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        var v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_task, viewGroup, false)
      //  v.setOnClickListener {listener?.onClick(list[i]) }
        return ViewHolder(v);
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var task: SettingsStorage.Settings = list[i]

        var descriptionRoute = task.stationFrom?.name + " - " + task.stationTo?.name + "  " + task.dateRoute

        viewHolder.idTask = task.id
        viewHolder.description.text = descriptionRoute
        viewHolder.itemView.setOnClickListener { listener?.onClick(list[i]) }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder : RecyclerView.ViewHolder {

        var idTask: Int? = null
        var description: TextView


        constructor(itemView: View) : super(itemView) {
            description = itemView.task_description as TextView


        }

    }

    interface OnItemTaskClickListener {
        fun onClick(settings: SettingsStorage.Settings)
    }

}
