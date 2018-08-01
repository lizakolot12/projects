package proj.kolot.uzsearch.task.edit

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.item_station.view.*
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.utils.inflate


class StationAutoCompleteAdapter : BaseAdapter, Filterable {

    val mPresenter: EditTaskPresenter
    private var mResults: List<proj.kolot.uzsearch.data.Station>? = null


    constructor(presenter: EditTaskPresenter, initial: List<proj.kolot.uzsearch.data.Station> = emptyList()) : super() {
        mResults = initial
        mPresenter = presenter
    }


    override fun getCount(): Int {
        return mResults?.size ?: 0
    }

    override fun getItem(index: Int): proj.kolot.uzsearch.data.Station {
        return mResults?.get(index) ?: proj.kolot.uzsearch.data.Station("undef", "undef")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView:View?, parent: ViewGroup): View {
        var convertViewNew = convertView
        convertViewNew = convertViewNew ?: parent.inflate(R.layout.item_station)
        val station = getItem(position)
        convertViewNew.stationName.text = station.name

        return convertViewNew
    }

    // Assign the data to the FilterResults


    override fun getFilter(): Filter {

        val filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {

                val filterResults =Filter.FilterResults()
                if (constraint != null) {
                    val stations = findStations(constraint.toString())
                    filterResults.values = stations
                    filterResults.count = stations.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    mResults = results.values as List<proj.kolot.uzsearch.data.Station>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }

        return filter
    }

    private fun findStations(nameStation: String): List<proj.kolot.uzsearch.data.Station> {

        return mPresenter.findStations(nameStation)
    }
}
