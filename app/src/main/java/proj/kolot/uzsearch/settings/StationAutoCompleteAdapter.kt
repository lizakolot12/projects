package proj.kolot.uzsearch.settings

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable

import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.utils.inflate
import kotlinx.android.synthetic.main.item_station.*
import kotlinx.android.synthetic.main.item_station.view.*
import proj.kolot.uzsearch.R.id.stationName


class StationAutoCompleteAdapter : BaseAdapter, Filterable {
    private val mContext: Context
    val mPresenter: SettingsPresenter
    private var mResults: List<Station>? = null


    constructor(mContext: Context, presenter: SettingsPresenter, initial: List<Station> = emptyList()) : super() {
        this.mContext = mContext
        mResults = initial
        mPresenter = presenter
    }


    override fun getCount(): Int {
        return mResults?.size?:0
    }

    override fun getItem(index: Int): Station {
        return mResults?.get(index)?: Station("undef", "undef")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewNew = convertView
        convertViewNew = convertViewNew?: parent.inflate(R.layout.item_station)
        val station = getItem(position)
        convertViewNew.stationName.text=station.name

        return convertViewNew
    }

    // Assign the data to the FilterResults


    override fun getFilter(): Filter {

            val filter = object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {

                    val filterResults = FilterResults()
                    if (constraint != null) {
                        val stations = findStations(constraint.toString())
                        filterResults.values = stations
                        filterResults.count = stations.size
                    }
                    return filterResults
                }

               override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if (results != null && results.count > 0) {
                        mResults = results.values as List<Station>
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }

            return filter
        }

    private fun findStations(nameStation: String): List<Station> {

        return mPresenter.findStations(nameStation)
    }
}
