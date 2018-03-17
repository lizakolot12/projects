package proj.kolot.uzsearch.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.train_item_layout.view.*
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.SeatType
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.settings.SettingsStorage


class ListTrainsAdapter : RecyclerView.Adapter<ListTrainsAdapter.ViewHolder> {

    private var mList: List<TransportRoute> = ArrayList()

    constructor(list: List<TransportRoute>) {
        mList = list
    }

    fun setList(list: List<TransportRoute>) {
        this.mList = list
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        var v = LayoutInflater.from(viewGroup.context).inflate(R.layout.train_item_layout, viewGroup, false);
        return ViewHolder(v);
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var train: TransportRoute = mList[i]
        var places: String = viewHolder.itemView.context.getString(R.string.places)
        var dateDeparture: String = SettingsStorage.LOCALE_NEUTRAL_DATE_TIME_FORMATTER.print(train.departureDate)
        var dateArrival: String = SettingsStorage.LOCALE_NEUTRAL_DATE_TIME_FORMATTER.print(train.arrivalDate)
        var seats: String = ""
        for (map: Map.Entry<SeatType, Int> in train.freeSeatsCountByType ?: emptyMap()) {
            seats += map.key.id + "=" + map.value + "     "
        }

        var description = train.name + "  \n" + places + ":" + seats + "   \n" +
                dateDeparture + "  \n" + dateArrival + "  "
        viewHolder.idTrain.text = train.id
        viewHolder.description.text = description


    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder : RecyclerView.ViewHolder {

        var idTrain: TextView
        var description: TextView


        constructor(itemView: View) : super(itemView) {
            idTrain = itemView.train_id as TextView
            description = itemView.train_description as TextView

        }

    }

}