package proj.kolot.uzsearch.task.edit

import android.widget.BaseAdapter
import android.widget.Filterable
import kotlinx.android.synthetic.main.item_station.view.*
import proj.kolot.uzsearch.utils.inflate


class StationAutoCompleteAdapter : BaseAdapter, Filterable {
    private val mContext: android.content.Context
    val mPresenter: EditTaskPresenter
    private var mResults: List<proj.kolot.uzsearch.data.Station>? = null


    constructor(mContext: android.content.Context, presenter: EditTaskPresenter, initial: List<proj.kolot.uzsearch.data.Station> = emptyList()) : super() {
        this.mContext = mContext
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

    override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        var convertViewNew = convertView
        convertViewNew = convertViewNew ?: parent.inflate(proj.kolot.uzsearch.R.layout.item_station)
        val station = getItem(position)
        convertViewNew.stationName.text = station.name

        return convertViewNew
    }

    // Assign the data to the FilterResults


    override fun getFilter(): android.widget.Filter {

        val filter = object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): android.widget.Filter.FilterResults {

                val filterResults = android.widget.Filter.FilterResults()
                if (constraint != null) {
                    val stations = findStations(constraint.toString())
                    filterResults.values = stations
                    filterResults.count = stations.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: android.widget.Filter.FilterResults?) {
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
