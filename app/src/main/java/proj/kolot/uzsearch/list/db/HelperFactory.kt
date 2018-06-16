package proj.kolot.uzsearch.list.db

import android.content.Context
import com.j256.ormlite.android.apptools.OpenHelperManager





object HelperFactory {

        var helper: DatabaseHelper? = null


        fun setHelper(context: Context) {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper::class.java)

        }

        fun releaseHelper() {
            OpenHelperManager.releaseHelper()
            helper = null
        }
    }
