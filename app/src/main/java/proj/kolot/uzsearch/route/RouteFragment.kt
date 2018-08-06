package proj.kolot.uzsearch.route

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_list_train.*
import kotlinx.android.synthetic.main.fragment_list_train.view.*
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.route.RouteAdapter.OnItemClickListener
import proj.kolot.uzsearch.task.edit.EditTaskActivity
import java.net.URL


//import com.arellomobile.mvp.presenter.ProvidePresenter

class RouteFragment : MvpFragment(), RouteView {


    @InjectPresenter(type = PresenterType.LOCAL, tag = "ListTrainPresenter")


    lateinit var presenter: RoutePresenter

    companion object {
        private val ARGUMENT_ID = "argument_id_route"

        fun newIntent(id: Int): RouteFragment {
            val fragment: RouteFragment = RouteFragment()
            val arg: Bundle = Bundle()
            arg.putInt(ARGUMENT_ID, id)
            fragment.arguments = arg
            return fragment
        }


    }

    override fun showErrorMessage(err: Error) {
        showErrorMessage(getMsgByCodeError(err))
    }

    fun changeData(id:Int) {
        Log.e("my test", " change data")
        presenter.changeData(id)
    }

    override fun showRouteName(name: String) {
        activity.title = name
    }

    private fun getMsgByCodeError(err: Error): String {
        var msg =
                when (err) {
                    Error.DATE_EXPIRED -> R.string.message_expired_date
                    Error.NOT_SAVED_DATA -> R.string.message_no_data_for_search
                    Error.EMPTY_LIST -> R.string.message_empty_list
                    else -> R.string.message_no_data_for_search
                }
        return getString(msg)
    }

    override fun hideErrorMessage() {
        message.visibility = View.GONE
        Log.e("my test", " hide error message")
    }

    override fun showErrorMessage(msg: String) {
        onTrainsLoaded(emptyList<TransportRoute>())
        message.visibility = View.VISIBLE
        message.text = msg
        Log.e("my test", " show error message " + msg)
    }

    override fun showProgress() {
        showErrorMessage(getString(R.string.message_searching_now))
        trainsView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        Log.e("my test", "show progress and will be on trains loaded with empty list")


    }

    override fun hideProgress() {
        progress.visibility = View.GONE
        Log.e("my test", " hide progress")
    }


    override fun onTrainsLoaded(trains: List<TransportRoute>) {
        Log.e("my test", " on traines loaded ")
        progress.visibility = View.GONE
        message.visibility = View.GONE
        trainsView.visibility = View.VISIBLE
        var adapter: RouteAdapter = trainsView.adapter as RouteAdapter
        adapter.onItemClickListener = object :OnItemClickListener{
            override fun onItemClick(transportRoute: TransportRoute) {
                presenter.onItemClick(transportRoute)
            }

        }
        adapter.setList(trains)
        updateView()
    }

    override fun showTrain(route: TransportRoute) {
        val url: URL? = route.url
        val intent = Intent(ACTION_VIEW)
        intent.setData(Uri.parse(url.toString()))
        startActivity(intent)
    }
    fun updateView() {
        Log.e("my test", " update view ")
        trainsView.adapter.notifyDataSetChanged()
    }

    private val trainsView: RecyclerView by lazy {
        trains_view.setHasFixedSize(true) // <-- Lazy executed!
        trains_view.layoutManager = LinearLayoutManager(activity)
        trains_view.adapter = RouteAdapter(emptyList())
        trains_view
    }

    override fun onResume() {
        super.onResume()
        Log.e("my test", " on resume list train fragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("my test", " on Create ROUTE FRAGMENT")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_train, container, false)
        presenter.showRoutes(arguments.getInt(ARGUMENT_ID))

        view.fab.setOnClickListener {
            presenter.editRoute(arguments.getInt(ARGUMENT_ID))
        }
        return view
    }

    override fun showEditRoute(id: Int) {
        startActivity(EditTaskActivity.newIntent(this.activity, id))
    }



}


