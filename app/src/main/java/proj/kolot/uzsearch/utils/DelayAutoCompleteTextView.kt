package proj.kolot.uzsearch.utils

class DelayAutoCompleteTextView : android.widget.AutoCompleteTextView {

    constructor(context: android.content.Context, attrs: android.util.AttributeSet) : super(context, attrs) {
        this.mAutoCompleteDelay = proj.kolot.uzsearch.utils.DelayAutoCompleteTextView.Companion.DEFAULT_AUTOCOMPLETE_DELAY
        this.mHandler = object : android.os.Handler() {
            override fun handleMessage(msg: android.os.Message) {
                super@DelayAutoCompleteTextView.performFiltering(msg.obj as CharSequence, msg.arg1)
            }
        }
    }

    private var mAutoCompleteDelay: Int
    private var mLoadingIndicator: android.widget.ProgressBar? = null

    private val mHandler: android.os.Handler

    fun setLoadingIndicator(progressBar: android.widget.ProgressBar) {
        mLoadingIndicator = progressBar
    }

    fun setAutoCompleteDelay(autoCompleteDelay: Int) {
        mAutoCompleteDelay = autoCompleteDelay
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        mLoadingIndicator?.visibility = android.view.View.VISIBLE
        mHandler.removeMessages(proj.kolot.uzsearch.utils.DelayAutoCompleteTextView.Companion.MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(mHandler.obtainMessage(proj.kolot.uzsearch.utils.DelayAutoCompleteTextView.Companion.MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay.toLong())
    }

    override fun onFilterComplete(count: Int) {
        mLoadingIndicator?.visibility = android.view.View.GONE
        super.onFilterComplete(count)
    }

    companion object {

        private val MESSAGE_TEXT_CHANGED = 100
        private val DEFAULT_AUTOCOMPLETE_DELAY = 750
    }
}
