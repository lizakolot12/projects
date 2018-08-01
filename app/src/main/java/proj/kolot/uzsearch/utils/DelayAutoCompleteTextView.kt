package proj.kolot.uzsearch.utils

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar

class DelayAutoCompleteTextView : AutoCompleteTextView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mAutoCompleteDelay = DelayAutoCompleteTextView.DEFAULT_AUTOCOMPLETE_DELAY
        this.mHandler = object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                super@DelayAutoCompleteTextView.performFiltering(msg.obj as CharSequence, msg.arg1)
            }
        }
    }

    private var mAutoCompleteDelay: Int
    private var mLoadingIndicator: ProgressBar? = null

    private val mHandler: Handler

    fun setLoadingIndicator(progressBar: ProgressBar) {
        mLoadingIndicator = progressBar
    }

    fun setAutoCompleteDelay(autoCompleteDelay: Int) {
        mAutoCompleteDelay = autoCompleteDelay
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        mLoadingIndicator?.visibility = android.view.View.VISIBLE
        mHandler.removeMessages(DelayAutoCompleteTextView.MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(mHandler.obtainMessage(DelayAutoCompleteTextView.MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay.toLong())
    }

    override fun onFilterComplete(count: Int) {
        mLoadingIndicator?.visibility = View.GONE
        super.onFilterComplete(count)
    }

    companion object {

        private val MESSAGE_TEXT_CHANGED = 100
        private val DEFAULT_AUTOCOMPLETE_DELAY = 750
    }
}
