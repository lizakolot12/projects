package proj.kolot.uzsearch

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Environment
import android.support.multidex.MultiDex
import android.util.Log
import org.slf4j.LoggerFactory
import pl.brightinventions.slf4android.LoggerConfiguration
import proj.kolot.uzsearch.di.AppComponent
import proj.kolot.uzsearch.di.ContextModule
import proj.kolot.uzsearch.di.DaggerAppComponent
import proj.kolot.uzsearch.di.SettingsStorageModule
import proj.kolot.uzsearch.di.TrainsRouteServiceModule
import pl.brightinventions.slf4android.FileLogHandlerConfiguration
import android.os.Environment.getExternalStorageDirectory
import java.io.File
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat.requestPermissions


//import android.support.multidex.MultiDex


class MainApplication : Application() {
    private val LOG = LoggerFactory.getLogger("MainApplication")

    companion object {
        lateinit var graph: AppComponent
        // lateinit var first: FirstComponent
        lateinit var instance: MainApplication
            private set

    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
        super.attachBaseContext(base)

    }


    override fun onCreate() {
        super.onCreate()
       // logging()
        instance = this
        graph = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .trainsRouteServiceModule(TrainsRouteServiceModule())
                .settingsStorageModule(SettingsStorageModule())
                .build()
        //first = DaggerAppComponent.builder().contextModule(ContextModule(this)).build()

    }

/*    private fun logging() {
        //add проверку разрешенний
        val fileHandler: FileLogHandlerConfiguration? = LoggerConfiguration.fileLogHandler(this)
        LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
            val appDirectory = File(Environment.getExternalStorageDirectory().toURI())
            val logDirectory = File(appDirectory.toString())

            fileHandler?.setFullFilePathPattern(logDirectory.toString() + "/my_log.%g.%u.log")
            LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler)
            if (fileHandler != null) {
                val logFileName: String? = fileHandler?.currentFileName
                LOG.debug("log initizializing " + logFileName);
                Log.e("my test", "file name = $logFileName")
            }
        } else {
            requestMultiplePermissions()
        }
    }

    fun requestMultiplePermissions() {
        var PERMISSION_REQUEST_CODE = 1
        ActivityCompat.requestPermissions(this.applicationContext,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE
        )
    }*/

}

