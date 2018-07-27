package proj.kolot.uzsearch.task.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater.from
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.item_task.view.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.Task

/**
 * Created by Kolot Liza on 6/6/18.
 */
class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    companion object {
        val LOCALE_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM HH:mm")
    }

   /* @Inject
    lateinit var context: Context

    init {
        MainApplication.graph.inject(this)

    }*/

    private var list: List<Task> = ArrayList()
    private var itemListener: TaskAdapter.OnItemTaskClickListener? = null
    private var btnRunListener: TaskAdapter.OnRunTaskClickListener? = null

    constructor(list: List<Task>) {
        this.list = list
    }

    fun setOnItemTaskClickListener(listener: TaskAdapter.OnItemTaskClickListener) {
        this.itemListener = listener
    }

    fun setBtnRunClickListener(listener: TaskAdapter.OnRunTaskClickListener) {
        this.btnRunListener = listener
    }

    fun setList(list: List<Task>) {
        this.list = list
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TaskAdapter.ViewHolder {
        var v = from(viewGroup.context).inflate(R.layout.item_task, viewGroup, false)
        //  v.setOnClickListener {listener?.onClick(list[i]) }
        return TaskAdapter.ViewHolder(v);
    }


    override fun onBindViewHolder(viewHolder: TaskAdapter.ViewHolder, i: Int) {
        var task: Task = list[i]

        var descriptionRoute = task.stationFrom?.name + " - " + task.stationTo?.name + "  " + LOCALE_DATE_TIME_FORMATTER.print(task.dateRoute)

        viewHolder.idTask = task.id
        viewHolder.description.text = descriptionRoute
        viewHolder.itemView.setOnClickListener { itemListener?.onItemClick(list[i]) }

        viewHolder.btnRun.setOnClickListener({
            btnRunListener?.onClick(task)

            viewHolder.btnRun.setImageResource(getImageForRunBtn(task.needPeriodicCheck))

        })
        viewHolder.btnRun.setImageResource(getImageForRunBtn(task.needPeriodicCheck))
        viewHolder.itemView.setOnLongClickListener { v: View ->
            var popupMenu: PopupMenu = PopupMenu(v.context, v)
            popupMenu.inflate(R.menu.menu_task_list)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.action_delete -> itemListener?.onItemLongClick(task)
                    }
                 //   popupMenu.dismiss()
                    return false
                }

            })
            popupMenu.show()
            true
        }
    }

    private fun getImageForRunBtn(needPeriodicCheck: Boolean): Int {
        var resourse = android.R.drawable.ic_media_play

        if (needPeriodicCheck) {
            resourse = android.R.drawable.ic_media_pause
        }
        return resourse
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder : RecyclerView.ViewHolder {

        var idTask: Int? = null
        var description: TextView
        var btnRun: ImageButton


        constructor(itemView: View) : super(itemView) {
            description = itemView.task_description as TextView
            btnRun = itemView.run as ImageButton


        }

    }

    interface OnRunTaskClickListener {
        fun onClick(task: Task)
    }

    interface OnItemTaskClickListener {
        fun onItemClick(task: Task)
        fun onItemLongClick(task: Task)
    }

}
